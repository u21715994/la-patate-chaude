use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
enum Result {
    Ok,
    Err(SubscribeError),
}
#[derive(Debug, Deserialize, Serialize)]
pub enum Challenge {
    MD5HashCash(ChallengeInputHash),
    MonstrousMaze(ChallengeInputMonstrous),
    RecoverSecret(ChallengeInputRecoverSecret),
}

#[derive(Debug, Deserialize, Serialize)]
pub enum Message {
    Hello,
    Welcome {
        version: u8,
    },
    Subscribe {
        name: String,
    },
    SubscribeResult(Result),
    PublicLeaderBoard(Vec<PublicPlayer>),
    Challenge(Challenge),
    ChallengeResult {
        answer: ChallengeAnswer,
        next_target: String,
    },
    ChallengeTimeout {
        message: String,
    },
    RoundSummary {
        challenge: String,
        chain: Vec<ReportedChallengeResult>,
    },
    EndOfGame {
        leader_board: Vec<PublicPlayer>,
    },
}

#[derive(Debug, Deserialize, Serialize)]
enum SubscribeError {
    AlreadyRegistered,
    InvalidName,
}

#[derive(Debug, Deserialize, Serialize, PartialEq)]
pub struct PublicPlayer {
    pub name: String,
    stream_id: String,
    pub score: i32,
    steps: u32,
    is_active: bool,
    total_used_time: f64,
}

#[derive(Debug, Deserialize, Serialize)]
enum ChallengeName {
    MD5HashCash,
    MonstrousMaze,
    RecoverSecret,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ChallengeInputHash {
    pub complexity: u8,
    pub message: String,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ChallengeInputMonstrous {
    pub grid: String,
    pub endurance: u32,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ChallengeInputRecoverSecret {
    pub word_count: usize,
    pub letters: String,
    pub tuple_sizes: Vec<usize>,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ChallengeOutputHash {
    pub seed: u64,
    pub hashcode: String,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct MonstrousMazeOutput {
    pub path: String,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct RecoverSecretOutput {
    pub secret_sentence: String,
}

#[derive(Debug, Deserialize, Serialize)]
pub enum ChallengeAnswer {
    MD5HashCash(ChallengeOutputHash),
    MonstrousMaze(MonstrousMazeOutput),
    RecoverSecret(RecoverSecretOutput),
}

#[derive(Debug, Deserialize, Serialize)]
enum ChallengeValue {
    Unreachable,
    Timeout,
    BadResult { used_time: f64, next_target: String },
    Ok { used_time: f64, next_target: String },
}

#[derive(Debug, Deserialize, Serialize)]
pub struct ReportedChallengeResult {
    name: String,
    value: ChallengeValue,
}
