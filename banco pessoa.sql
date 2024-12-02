CREATE DATABASE educa;
USE educa;

CREATE TABLE usuario (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    senha VARCHAR(64) NOT NULL
);

CREATE TABLE escola (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    endereco TEXT NOT NULL
);

CREATE TABLE pessoa (
    login VARCHAR(50) NOT NULL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    telefone VARCHAR(15),
    tipo ENUM('ALUNO', 'PROFESSOR', 'FUNCIONARIO', 'DIRETOR') NOT NULL,
    id_escola INT NOT NULL,
    FOREIGN KEY (id_escola) REFERENCES escola(id) ON DELETE CASCADE,
    FOREIGN KEY (login) REFERENCES usuario(login) ON DELETE CASCADE
);

CREATE TABLE turmas (
    sigla VARCHAR(2) NOT NULL,
    materia VARCHAR(100) NOT NULL,
    PRIMARY KEY (sigla, materia)
);

CREATE TABLE aluno (
    matricula INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    turma VARCHAR(2),
    ausencias INT DEFAULT 0,
    FOREIGN KEY (login) REFERENCES pessoa(login) ON DELETE CASCADE,
    FOREIGN KEY (turma) REFERENCES turmas(sigla) ON DELETE SET NULL
);

CREATE TABLE professor (
    numero_professor INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    materia VARCHAR(50),
    turmas TEXT,
    ausencias INT DEFAULT 0,
    FOREIGN KEY (login) REFERENCES pessoa(login) ON DELETE CASCADE
);

CREATE TABLE funcionario (
    numero_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    funcao VARCHAR(50),
    ausencias INT DEFAULT 0,
    FOREIGN KEY (login) REFERENCES pessoa(login) ON DELETE CASCADE
);

CREATE TABLE boletim (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login_aluno VARCHAR(50) NOT NULL,
    materia VARCHAR(100) NOT NULL,
    nota DECIMAL(5, 2) NOT NULL,
    presenca DECIMAL(5, 2) NOT NULL,
    FOREIGN KEY (login_aluno) REFERENCES aluno(login) ON DELETE CASCADE
);

CREATE TABLE respostas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    login_usuario VARCHAR(50) NOT NULL,
    tipo_usuario ENUM('ALUNO', 'PROFESSOR', 'FUNCIONARIO') NOT NULL,
    nome_formulario VARCHAR(100) NOT NULL,
    q1 INT DEFAULT -1,
    q2 INT DEFAULT -1,
    q3 INT DEFAULT -1,
    q4 INT DEFAULT -1,
    q5 INT DEFAULT -1,
    q6 INT DEFAULT -1,
    q7 INT DEFAULT -1,
    q8 INT DEFAULT -1,
    q9 INT DEFAULT -1,
    q10 INT DEFAULT -1,
    q11 INT DEFAULT -1,
    q12 INT DEFAULT -1,
    q13 INT DEFAULT -1,
    q14 INT DEFAULT -1,
    q15 INT DEFAULT -1,
    FOREIGN KEY (login_usuario) REFERENCES usuario(login) ON DELETE CASCADE
);

INSERT INTO escola (nome, endereco) 
VALUES ('Escola Estadual Modelo', 'Rua Principal, 123, Cidade Exemplo');

INSERT INTO turmas (sigla, materia) 
VALUES 
('3B', 'Matemática'),
('3B', 'Português'),
('3B', 'História'),
('3B', 'Ciências'),
('2A', 'Matemática'),
('2A', 'História'),
('1C', 'Ciências');

INSERT INTO usuario (login, senha) VALUES 
('aluno123', SHA2('123', 256)),
('prof123', SHA2('123', 256)),
('diretor123', SHA2('123', 256)),
('func123', SHA2('123', 256));

INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo, id_escola) 
VALUES 
('aluno123', 'João Silva', '123.456.789-00', '2005-06-15', '(11) 91234-5678', 'ALUNO', 1),
('prof123', 'Maria Santos', '456.789.123-00', '1980-08-20', '(11) 99988-7766', 'PROFESSOR', 1),
('diretor123', 'Carlos Almeida', '789.123.456-00', '1970-05-15', '(11) 98877-6655', 'DIRETOR', 1),
('func123', 'Ana Silva', '321.654.987-00', '1985-03-25', '(11) 97766-5544', 'FUNCIONARIO', 1);

INSERT INTO aluno (login, turma, ausencias) 
VALUES ('aluno123', '3B', 2);

INSERT INTO professor (login, materia, turmas, ausencias) 
VALUES ('prof123', 'Matemática', '3B,2A', 0);

INSERT INTO funcionario (login, funcao, ausencias) 
VALUES ('func123', 'Secretaria', 0);

INSERT INTO boletim (login_aluno, materia, nota, presenca) 
VALUES 
('aluno123', 'Matemática', 8.5, 95.0),
('aluno123', 'Português', 7.0, 90.0),
('aluno123', 'História', 9.0, 98.0),
('aluno123', 'Ciências', 6.5, 85.0);