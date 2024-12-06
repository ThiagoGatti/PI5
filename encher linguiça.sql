use educa;

-- Adicionando mais turmas
INSERT INTO turmas (sigla, materias) VALUES
('1A', '["Português", "Ciências"]'),
('1B', '["História", "Matemática"]'),
('2B', '["Matemática", "Geografia", "História"]'),
('3A', '["Física", "Química", "Biologia"]');

-- Adicionando mais usuários
INSERT INTO usuario (login, senha) VALUES 
('aluno124', SHA2('senha123', 256)),
('aluno125', SHA2('senha123', 256)),
('prof124', SHA2('senha123', 256)),
('prof125', SHA2('senha123', 256)),
('func124', SHA2('senha123', 256));

-- Adicionando mais pessoas
INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo, id_escola) 
VALUES 
('aluno124', 'Paula Mendes', '112.223.334-00', '2006-04-12', '(11) 93456-7890', 'ALUNO', 1),
('aluno125', 'Pedro Alves', '223.334.445-00', '2007-09-23', '(11) 94567-8901', 'ALUNO', 1),
('prof124', 'José Lima', '334.445.556-00', '1975-11-10', '(11) 95678-9012', 'PROFESSOR', 1),
('prof125', 'Clara Nogueira', '445.556.667-00', '1982-03-15', '(11) 96789-0123', 'PROFESSOR', 1),
('func124', 'Roberto Silva', '556.667.778-00', '1988-06-08', '(11) 97890-1234', 'FUNCIONARIO', 1);

-- Associando alunos a turmas
INSERT INTO aluno (login, turma) 
VALUES 
('aluno124', '1A'),
('aluno125', '2B');

-- Associando professores a matérias e turmas
INSERT INTO professor (login, materia, turmas) 
VALUES 
('prof124', 'Geografia', '["1A", "2B"]'),
('prof125', 'Química', '["3A"]');

-- Adicionando novos funcionários
INSERT INTO funcionario (login, funcao) 
VALUES 
('func124', 'Merendeira');

-- Associando diretores a novas escolas
INSERT INTO usuario (login, senha) 
VALUES 
('diretor3', SHA2('123', 256)),
('diretor4', SHA2('123', 256));

INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo, id_escola) 
VALUES 
('diretor3', 'Marcelo Souza', '667.778.889-00', '1975-08-20', '(11) 92345-6789', 'DIRETOR', 1),
('diretor4', 'Fernanda Lima', '778.889.990-00', '1985-12-05', '(11) 93456-7890', 'DIRETOR', 1);

-- Preenchendo a tabela boletim com dados variados
INSERT INTO boletim (login_aluno, materia, nota, presenca) VALUES
('aluno124', 'Português', 9.0, 100),
('aluno124', 'Ciências', 8.0, 90),
('aluno125', 'Matemática', 7.5, 85),
('aluno125', 'História', 8.5, 95),
('aluno125', 'Geografia', 9.0, 92),
('aluno124', 'Matemática', 6.0, 80),
('aluno124', 'História', 8.0, 85),
('aluno125', 'Português', 9.5, 96);

-- Inserindo respostas de alunos para o formulário "Autonomia e Protagonismo"
INSERT INTO respostas (login_usuario, tipo_usuario, nome_formulario, q1, q2, q3, q4, q5, q6, q7, q8, q9, q10) VALUES
('aluno123', 'ALUNO', 'Autonomia e Protagonismo', 8, 7, 9, 6, 8, 7, 9, 8, 9, 7),
('aluno124', 'ALUNO', 'Autonomia e Protagonismo', 9, 8, 7, 8, 9, 8, 9, 9, 9, 8),
('aluno125', 'ALUNO', 'Autonomia e Protagonismo', 7, 6, 8, 7, 6, 7, 6, 8, 7, 7);

-- Inserindo respostas de alunos para o formulário "Clima Escolar"
INSERT INTO respostas (login_usuario, tipo_usuario, nome_formulario, q1, q2, q3, q4, q5, q6) VALUES
('aluno123', 'ALUNO', 'Clima Escolar', 8, 9, 7, 8, 9, 7),
('aluno124', 'ALUNO', 'Clima Escolar', 9, 8, 8, 9, 8, 9),
('aluno125', 'ALUNO', 'Clima Escolar', 7, 6, 7, 8, 7, 8);

-- Inserindo respostas de professores para o formulário "Condições de Trabalho"
INSERT INTO respostas (login_usuario, tipo_usuario, nome_formulario, q1, q2, q3, q4) VALUES
('prof123', 'PROFESSOR', 'Condições de Trabalho', 7, 8, 9, 6),
('prof124', 'PROFESSOR', 'Condições de Trabalho', 8, 9, 7, 8);

-- Inserindo respostas de professores para o formulário "Qualidade da Educação"
INSERT INTO respostas (login_usuario, tipo_usuario, nome_formulario, q1, q2, q3, q4, q5, q6, q7, q8, q9, q10) VALUES
('prof123', 'PROFESSOR', 'Qualidade da Educação', 8, 7, 9, 6, 8, 7, 9, 8, 9, 7),
('prof124', 'PROFESSOR', 'Qualidade da Educação', 9, 8, 7, 8, 9, 8, 9, 9, 9, 8);

-- Inserindo respostas de funcionários para o formulário "Satisfação no Trabalho"
INSERT INTO respostas (login_usuario, tipo_usuario, nome_formulario, q1, q2, q3, q4) VALUES
('func123', 'FUNCIONARIO', 'Satisfação no Trabalho', 8, 9, 7, 8),
('func124', 'FUNCIONARIO', 'Satisfação no Trabalho', 9, 8, 8, 9);

-- Inserindo respostas de funcionários para o formulário "Eficiência da Gestão"
INSERT INTO respostas (login_usuario, tipo_usuario, nome_formulario, q1, q2, q3, q4) VALUES
('func123', 'FUNCIONARIO', 'Eficiência da Gestão', 8, 9, 7, 8),
('func124', 'FUNCIONARIO', 'Eficiência da Gestão', 9, 8, 8, 9);

-- Inserir os alunos no sistema
INSERT INTO usuario (login, senha) VALUES
('aluno1A1', SHA2('123', 256)),
('aluno1A2', SHA2('123', 256)),
('aluno1A3', SHA2('123', 256));

INSERT INTO pessoa (login, nome, cpf, data_nascimento, telefone, tipo, id_escola) VALUES
('aluno1A1', 'Pedro Oliveira', '111.222.333-44', '2007-02-15', '(11) 91234-5670', 'ALUNO', 1),
('aluno1A2', 'Ana Costa', '222.333.444-55', '2007-05-10', '(11) 91234-5671', 'ALUNO', 1),
('aluno1A3', 'Marcos Souza', '333.444.555-66', '2007-09-20', '(11) 91234-5672', 'ALUNO', 1);

INSERT INTO aluno (login, turma) VALUES
('aluno1A1', '1A'),
('aluno1A2', '1A'),
('aluno1A3', '1A');