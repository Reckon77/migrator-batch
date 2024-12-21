INSERT INTO users (id, name, email, phone, last_updated) VALUES
(1, 'John Doe', 'john.doe@example.com', '1234567890', '1970-01-01 00:00:00'),
(2, 'Jane Smith', 'jane.smith@example.com', '2345678901', '1970-01-01 00:00:00'),
(3, 'Michael Johnson', 'michael.johnson@example.com', '3456789012', '1970-01-01 00:00:00'),
(4, 'Emily Davis', 'emily.davis@example.com', '4567890123', '1970-01-01 00:00:00'),
(5, 'Chris Brown', 'chris.brown@example.com', '5678901234', '1970-01-01 00:00:00');

INSERT INTO accounts (id, account_type, balance, user_id) VALUES
(1, 'Savings', 1500.00, 1),
(2, 'Checking', 2000.00, 1),
(3, 'Savings', 2500.00, 2),
(4, 'Checking', 3000.00, 2),
(5, 'Savings', 3500.00, 3),
(6, 'Checking', 4000.00, 3),
(7, 'Savings', 4500.00, 4),
(8, 'Checking', 5000.00, 4),
(9, 'Savings', 5500.00, 5),
(10, 'Checking', 6000.00, 5);

INSERT INTO transactions (id, transaction_type, amount, timestamp, account_id) VALUES
(1, 'Deposit', 500.00, '2023-03-01 10:00:00', 1),
(2, 'Withdrawal', 200.00, '2023-03-01 11:00:00', 1),
(3, 'Deposit', 1500.00, '2023-03-01 12:00:00', 2),
(4, 'Withdrawal', 1000.00, '2023-03-02 10:00:00', 2),
(5, 'Deposit', 2500.00, '2023-03-02 11:00:00', 3),
(6, 'Withdrawal', 500.00, '2023-03-03 12:00:00', 3),
(7, 'Deposit', 3000.00, '2023-03-03 13:00:00', 4),
(8, 'Withdrawal', 1500.00, '2023-03-04 14:00:00', 4),
(9, 'Deposit', 3500.00, '2023-03-04 15:00:00', 5),
(10, 'Withdrawal', 1000.00, '2023-03-05 16:00:00', 5),
(11, 'Deposit', 4000.00, '2023-03-05 17:00:00', 6),
(12, 'Withdrawal', 1200.00, '2023-03-06 18:00:00', 6),
(13, 'Deposit', 4500.00, '2023-03-06 19:00:00', 7),
(14, 'Withdrawal', 1300.00, '2023-03-07 20:00:00', 7),
(15, 'Deposit', 5000.00, '2023-03-07 21:00:00', 8);
