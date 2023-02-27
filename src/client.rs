use std::borrow::Borrow;
use std::env;
use std::io::prelude::*;
use std::net::TcpStream;
use md5::Digest;
use serde_json::Value;
use std::string::String;
mod message;
use message::Message;
use crate::message::Challenge::{MD5HashCash, RecoverSecret};
use crate::message::Challenge::MonstrousMaze;
use crate::message::{ChallengeInputHash, ChallengeInputMonstrous, ChallengeInputRecoverSecret, ChallengeOutputHash, MonstrousMazeOutput, PublicPlayer, RecoverSecretOutput};
mod challenge_hash;
mod challenge_monstrous_maze;
mod challenge_recover_secret;
mod challenge;
use challenge::Challenge;
use crate::challenge_hash::{MD5HashCashChallenge, MD5HashCashInput};
use message::ChallengeAnswer;
use crate::challenge_monstrous_maze::{monstrous_maze, MonstrousMazeInput};
use crate::challenge_recover_secret::{recover_secret, RecoverSecretInput};

fn main() {
    let args: Vec<String> = env::args().collect();
    let name_player = &args[1];
    let mut stream = TcpStream::connect("127.0.0.1:7878").unwrap();
    let message = r#""Hello""#;

    let len = message.len() as u32;
    stream.write(&len.to_be_bytes()).unwrap(); // on écrit le préfixe (taille du prochain message)
    stream.write(message.as_bytes()).unwrap(); // puis le message en tant que tel
    let mut my_score = 0;
    let mut list_player: Vec<PublicPlayer> = Vec::new();
    //let mut stream = TcpStream::connect("127.0.0.1:7878").unwrap();

// Envoyer le message Hello au serveur

    loop {
        /*let hello_message = Message::Hello;
        let hello_json = serde_json::to_string(&hello_message).unwrap();
        let hello_len = hello_json.len() as u32;
        let hello_len_buf = hello_len.to_be_bytes();
        stream.write_all(&hello_len_buf).unwrap();
        stream.write_all(hello_json.as_bytes()).unwrap();*/

        let mut len_buf = [0u8; 4];
        let _ = stream.read_exact(&mut len_buf).unwrap();
        let len = u32::from_be_bytes(len_buf);

        let mut message_buf = vec![0u8; len as usize];
        let _ = stream.read_exact(&mut message_buf).unwrap();

        let json_value: Value = serde_json::from_slice(&message_buf).unwrap();
        /*let mut message: Message;
        if json_value == Message::SubscribeResult{
            json_value.Ok != ""
        }else{
            message: Message = serde_json::from_value(json_value).unwrap();
        }*/
        let message: Message = serde_json::from_value(json_value).unwrap();
        match message {
            Message::Welcome { version } => {
                // handle "Welcome" message with version field

                // Envoyer le message Subscribe au serveur
                let subscribe_message = Message::Subscribe {name: name_player.to_string()};
                let subscribe_json = serde_json::to_string(&subscribe_message).unwrap();
                let subscribe_len = subscribe_json.len() as u32;
                let subscribe_len_buf = subscribe_len.to_be_bytes();
                stream.write_all(&subscribe_len_buf).unwrap();
                stream.write_all(subscribe_json.as_bytes()).unwrap();
            },
            Message::PublicLeaderBoard(players)=>{
                for player in players{
                    if player.name != *name_player && !is_present(&player.name, &list_player){
                        list_player.push(player);
                    }else if player.name == *name_player{
                        my_score = player.score;
                    }
                }
            },
            Message::ChallengeTimeout { message }=>{
                println!("Vous avez ete vire");
                break;
            },
            Message::Challenge(RecoverSecret(ChallengeInputRecoverSecret{word_count, letters, tuple_sizes}))=>{
                let input = RecoverSecretInput {
                    word_count,
                    letters,
                    tuple_sizes,
                };
                let output = recover_secret(input);
                let best_player = attack_best_player(name_player.to_string(), my_score, &list_player);
                let result_message = Message::ChallengeResult {
                    answer: ChallengeAnswer::RecoverSecret(RecoverSecretOutput{
                        secret_sentence: output.secret_sentence,
                    }),
                    next_target: best_player
                };
                let result_json = serde_json::to_string(&result_message).unwrap();
                let result_len = result_json.len() as u32;
                let result_len_buf = result_len.to_be_bytes();
                stream.write_all(&result_len_buf).unwrap();
                stream.write_all(result_json.as_bytes()).unwrap();
            }
            Message::Challenge(MonstrousMaze(ChallengeInputMonstrous{grid, endurance}))=>{
                let input: MonstrousMazeInput = MonstrousMazeInput{
                    grid,
                    endurance
                };
                let output = monstrous_maze(input);
                let best_player = attack_best_player(name_player.to_string(), my_score, &list_player);
                let result_message = Message::ChallengeResult {
                    answer: ChallengeAnswer::MonstrousMaze(MonstrousMazeOutput{
                        path: output.path
                    }),
                    next_target: best_player
                };
                let result_json = serde_json::to_string(&result_message).unwrap();
                let result_len = result_json.len() as u32;
                let result_len_buf = result_len.to_be_bytes();
                stream.write_all(&result_len_buf).unwrap();
                stream.write_all(result_json.as_bytes()).unwrap();
            },
            Message::Challenge(MD5HashCash(ChallengeInputHash{ complexity , message }))=>{
                let input: MD5HashCashInput = MD5HashCashInput{
                    complexity: u32::from(complexity),
                    message
                };
                let challenge = MD5HashCashChallenge::new(input);
                let output = challenge.solve();
                let best_player = attack_best_player(name_player.to_string(), my_score, &list_player);
                let result_message = Message::ChallengeResult {
                    answer: ChallengeAnswer::MD5HashCash(ChallengeOutputHash{
                        seed: output.seed,
                        hashcode: output.hashcode,
                    }),
                    next_target: best_player
                };
                let result_json = serde_json::to_string(&result_message).unwrap();
                let result_len = result_json.len() as u32;
                let result_len_buf = result_len.to_be_bytes();
                stream.write_all(&result_len_buf).unwrap();
                stream.write_all(result_json.as_bytes()).unwrap();
            }
            Message::RoundSummary {challenge, chain}=>{
                //println!("Challenge de la partie {}", challenge);
                //println!("Résume du round {:?}", chain);
            },
            Message::EndOfGame {leader_board}=>{
                println!("Fin de la partie");
                break;
            }
            _ => {}
        }
    }
}

fn is_present(name: &String, players: &Vec<PublicPlayer>) -> bool{
    for player in players{
        if player.name == *name{
            return true;
        }
    }
    false
}

fn attack_best_player(name: String, mut score: i32, list_player: &Vec<PublicPlayer>)-> String{
    let mut best_player_name = String::from("");
    for player in list_player{
        if score < player.score && name != player.name{
            best_player_name = player.name.clone();
            score = player.score;
        }
    }
    best_player_name
}
