//ajouter ces bibliotheques externes au projet
use md5::Digest;
use serde::Deserializer;
use serde_json::Value;
use std::collections::HashMap;
use std::hash::{Hash, Hasher};
use std::num::ParseIntError;
use std::str::FromStr;
//use crate::Challenge;
use crate::challenge::Challenge;

/// Ajout des bibliothèques externes nécessaires
extern crate md5;
extern crate rand;

/// Structure qui représente les données en entrée du challenge
#[derive(Debug, Clone, Eq, PartialEq)]
pub struct MD5HashCashInput {
    /// complexité en bits
    pub complexity: u32,
    /// message à signer
    pub message: String,
}

/// Structure qui représente les données en sortie du challenge
#[derive(Debug, Clone, Eq, PartialEq)]
pub struct MD5HashCashOutput {
    /// graine utilisée pour résoudre le challenge
    pub seed: u64,
    /// hashcode trouvé en utilisant la graine + le message
    pub hashcode: String,
}

/// Implémentation de la fonction Hash pour la structure MD5HashCashInput
impl Hash for MD5HashCashInput {
    fn hash<H: Hasher>(&self, state: &mut H) {
        self.complexity.hash(state);
        self.message.hash(state);
    }
}

/// Implémentation de la fonction FromStr pour la structure MD5HashCashInput
impl FromStr for MD5HashCashInput {
    type Err = String;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let mut parts = s.splitn(2, " ");
        let complexity = parts.next().ok_or("Missing complexity".to_string())?;
        let message = parts.next().ok_or("Missing message".to_string())?;

        Ok(MD5HashCashInput {
            complexity: complexity.parse().unwrap(),
            message: message.to_string(),
        })
    }
}

/// converti le caractère hexadécimal en binaire
fn format_binary(c: char) -> String {
    if c == '0' {
        return "0000".to_string();
    } else if c == '1' {
        return "0001".to_string();
    } else if c == '2' {
        return "0010".to_string();
    } else if c == '3' {
        return "0011".to_string();
    } else if c == '4' {
        return "0100".to_string();
    } else if c == '5' {
        return "0101".to_string();
    } else if c == '6' {
        return "0110".to_string();
    } else if c == '7' {
        return "0111".to_string();
    } else if c == '8' {
        return "1000".to_string();
    } else if c == '9' {
        return "1001".to_string();
    } else if c == 'A' {
        return "1010".to_string();
    } else if c == 'B' {
        return "1011".to_string();
    } else if c == 'C' {
        return "1100".to_string();
    } else if c == 'D' {
        return "1101".to_string();
    } else if c == 'E' {
        return "1110".to_string();
    } else if c == 'F' {
        return "1111".to_string();
    };
    return "1".to_string();
}

/// converti un hexadécimal en binaire
fn convert_hex_to_binary(hex: &String) -> String {
    let mut binary = "".to_string();
    for i in hex.chars() {
        binary = binary.to_owned() + &format_binary(i);
    }
    binary
}

fn verify_bit_zero(number: u32, binary: String) -> bool {
    for i in 0..number {
        if binary.chars().nth(i as usize) != Some('0') {
            return false;
        }
    }
    return true;
}

/// Fonction principale qui résout le challenge
fn solve_md5_hash_cash(input: MD5HashCashInput) -> MD5HashCashOutput {
    /// Map qui stocke les résultats déjà calculés pour éviter de refaire les calculs
    let mut cache: HashMap<MD5HashCashInput, MD5HashCashOutput> = HashMap::new();
    let mut output: MD5HashCashOutput = MD5HashCashOutput {
        seed: 1,
        hashcode: "".to_string(),
    };

    /// Génère une valeur de graine aléatoire
    let mut seed: u64 = 0;

    /// Tant qu'on n'a pas trouvé une valeur de graine qui résout le challenge
    loop {
        let mut seed_binary = format!("{:X}", seed);
        if seed_binary.len() < 16 {
            let zero_to_add = 16 - seed_binary.len();
            for i in 0..zero_to_add {
                seed_binary = "0".to_string() + &seed_binary;
            }
        }
        /// Calcul du hashcode en utilisant la graine + le message
        let mut binary = md5::Md5::new();
        binary.update(seed_binary.to_owned() + &input.message.to_string());
        let binary_bin = binary.finalize();
        let hashcode = format!("{:X}", binary_bin);
        /// Convertit le hashcode en binaire
        let hashcode_bin = convert_hex_to_binary(&hashcode);

        ///regarder si les complexity bits sont egaux à 0 convertir hashcode en bits
        let hashcode_clone = hashcode.clone();
        // Vérifie si le hashcode comprend au moins "complexity" bits égaux à 0
        if verify_bit_zero(input.complexity, hashcode_bin) {
            ///Stocke le résultat dans la map pour éviter de refaire les calculs
            let seed_binary_64 = u64::from_str_radix(&seed_binary, 16).unwrap();
            output.seed = seed_binary_64;
            output.hashcode = hashcode_clone;
            break;
        }

        // Génère une nouvelle valeur de graine aléatoire
        seed = seed + 1;
    }

    /// Retourne le résultat du challenge
    println!("{:?}", output);
    output
}

/// Implémentation du trait Challenge pour la structure MD5HashCash
pub struct MD5HashCashChallenge {
    md5hash_cash_input: MD5HashCashInput,
    md5hash_cash_ouput: MD5HashCashOutput,
}

impl Challenge for MD5HashCashChallenge {
    type Input = MD5HashCashInput;
    type Output = MD5HashCashOutput;
    /// Le nom du challenge est "MD5 Hash Cash"
    fn name() -> String {
        "MD5 Hash Cash".to_string()
    }

    /// Crée un nouveau challenge à partir des données en entrée
    fn new(hash_input: Self::Input) -> Self {
        let hash_input_clone = hash_input.clone();
        MD5HashCashChallenge {
            md5hash_cash_input: hash_input,
            md5hash_cash_ouput: solve_md5_hash_cash(hash_input_clone),
        }
    }

    /// Résout le challenge
    fn solve(&self) -> Self::Output {
        self.md5hash_cash_ouput.clone()
    }

    /// Vérifie qu'une sortie est valide pour le challenge
    fn verify(&self, output: &Self::Output) -> bool {
        // Vérifie si les données en sortie sont égales à celles calculées lors de la création du challenge
        self.md5hash_cash_ouput == *output
    }
}

#[test]
fn test_md5_hash_cash_input_from_str_valid_input() {
    let input_str = "5 hello".to_string();
    let expected_input = MD5HashCashInput {
        complexity: 5,
        message: "hello".to_string(),
    };
    assert_eq!(
        input_str.parse::<MD5HashCashInput>().unwrap(),
        expected_input
    );
}

#[test]
fn test_md5_hash_cash_input_from_str_missing_message() {
    let input_str = "00000".to_string();
    let expected_err = "Missing message".to_string();
    assert_eq!(
        input_str.parse::<MD5HashCashInput>().unwrap_err(),
        expected_err
    );
}
