pub struct RecoverSecretInput {
    pub word_count: usize,
    pub letters: String,
    pub tuple_sizes: Vec<usize>,
}

pub struct RecoverSecretOutput {
    pub secret_sentence: String,
}

pub fn recover_secret(input:RecoverSecretInput)->RecoverSecretOutput{
    let mut x=1;
    let mut secret=Vec::new();
    while x<=secret.len() || x==1 {
        let mut k=0;
        let mut m=0;
        let mut temp=Vec::new();
        for i in 0..input.tuple_sizes.len() {
            if secret.len()==0 {
                temp.push(input.letters.chars().nth(k).unwrap());
            }
            for j in k..(k+input.tuple_sizes.get(i).unwrap()) {
                if (secret.len()!=0) && (input.letters.chars().nth(j).unwrap()==*secret.get(secret.len()-x).unwrap()) {
                    if (j+1)<(k+input.tuple_sizes.get(i).unwrap()) {
                        temp.push(input.letters.chars().nth(j+1).unwrap());
                    }
                }
                m=j;
            }
            k=m+1;
        }
        k=0;
        m=0;
        for i in 0..input.tuple_sizes.len() {
            for j in k..(k+input.tuple_sizes.get(i).unwrap()) {
                if temp.contains(&input.letters.chars().nth(j).unwrap()) {
                    if (j!=0) && ((j-1)>=k) && (!secret.contains(&input.letters.chars().nth(j-1).unwrap())) {
                        while temp.contains(&input.letters.chars().nth(j).unwrap()) {
                            let index=temp.iter().position(|x| *x == input.letters.chars().nth(j).unwrap()).unwrap();
                            temp.remove(index);
                        }
                    }
                }
                m=j;
            }
            k=m+1;
        }
        if temp.len()==0 {
            x+=1;
        }
        for l in 0..temp.len() {
            if !(secret.contains(temp.get(l).unwrap())) {
                secret.push(*temp.get(l).unwrap());
                x=1;
            }
        }
        if (temp.len()>0) && (x!=1) {
            x+=1;
        }
    }
    let s: String=secret.iter().collect();
    let output=RecoverSecretOutput{
        secret_sentence: s,
    };
    return output;
}

fn main(){
    let tuples=vec![3,4,5,7,7,3];
    let tuples2=vec![6,8,4,6,4,7,8,9,6,9,8,7,5,7,6,6,9,5,4,5,4];
    let r=RecoverSecretInput{
        word_count: 2,
        letters: "t cCehuCethoCeschouC'schout h".to_string(),
        tuple_sizes: tuples,
    };
    let r2=RecoverSecretInput{
        word_count: 1,
        letters: "WvyOAlxafUzleiSOl9xayBeHTmy9xWTU5lMW4nUO5lMWRajn2BiHSRUzy5afnUz5wlexWrm5wlBWr4mAlBrUmzHxTUzwlHrfTwBeSRmzlMSRfoUOAe9S4oUiraOiramzM5w3l".to_string(),
        tuple_sizes: tuples2,
    };
    println!("{}",recover_secret(r).secret_sentence);
    println!("{}",recover_secret(r2).secret_sentence);
}