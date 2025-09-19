-- Insert users with role
-- INSERT INTO users (dob, country, email, fname, full_name, lname, mobile, password, ssn, role) VALUES
--                                                                                                   ('1990-01-01', 'USA', 'user1@example.com', 'John', 'John Doe', 'Doe', '1234567890', 'password', '111-11-1111', 'USER'),
--                                                                                                   ('1991-02-02', 'USA', 'user2@example.com', 'Jane', 'Jane Smith', 'Smith', '0987654321', 'password', '222-22-2222', 'ADMIN'),
--                                                                                                   ('1992-03-03', 'USA', 'user3@example.com', 'Bob', 'Bob Johnson', 'Johnson', '1122334455', 'password', '333-33-3333', 'USER'),
--                                                                                                   ('1993-04-04', 'USA', 'user4@example.com', 'Alice', 'Alice Brown', 'Brown', '5566778899', 'password', '444-44-4444', 'USER'),
--                                                                                                   ('1994-05-05', 'USA', 'user5@example.com', 'Eve', 'Eve Davis', 'Davis', '6677889900', 'password', '555-55-5555', 'USER');
INSERT INTO authorize_users (emp_id, email, role) VALUES
                                                      (455, 'a@admin.com', 'ADMIN'),
                                                      (105, 'u@user.com', 'USER'),
                                                      (3, 'user3@example.com', 'USER');

