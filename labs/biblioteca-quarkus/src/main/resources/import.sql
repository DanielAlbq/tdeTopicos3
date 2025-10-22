

-- Inserção de Autores
INSERT INTO autores(id, nome, email, data_nascimento, biografia) VALUES
(1, 'J.K. Rowling', 'jk@email.com', '1965-07-31', 'Autora britânica, mais conhecida pela série de fantasia Harry Potter.'),
(2, 'J.R.R. Tolkien', 'tolkien@email.com', '1892-01-03', 'Escritor, professor universitário e filólogo britânico, mais conhecido por O Hobbit e O Senhor dos Anéis.'),
(3, 'George Orwell', 'orwell@email.com', '1903-06-25', 'Romancista, ensaísta e jornalista britânico, mais conhecido por 1984 e A Revolução dos Bichos.');

-- Inserção de Livros

INSERT INTO livros(id, titulo, isbn, data_publicacao, numero_paginas, disponivel, autor_id) VALUES
(1, 'Harry Potter e a Pedra Filosofal', '978-8532530277', '1997-06-26', 223, true, 1),
(2, 'O Hobbit', '978-8595084742', '1937-09-21', 336, false, 2),
(3, 'A Revolução dos Bichos', '978-8535914856', '1945-08-17', 152, true, 3),
(4, 'Harry Potter e a Câmara Secreta', '978-8532530284', '1998-07-02', 251, true, 1);

INSERT INTO emprestimos(id, nome_usuario, email_usuario, data_emprestimo, data_devolucao_prevista, data_devolucao, livro_id) VALUES
(1, 'Ana Silva', 'ana.silva@teste.com', '2025-10-15', '2025-10-29', NULL, 2);

SELECT setval('autores_id_seq', (SELECT MAX(id) FROM autores));
SELECT setval('livros_id_seq', (SELECT MAX(id) FROM livros));
SELECT setval('emprestimos_id_seq', (SELECT MAX(id) FROM emprestimos));