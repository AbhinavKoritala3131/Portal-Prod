INSERT INTO users (
    fname,
    lname,
    full_name,
    email,
    password,
    country,
    mobile,
    ssn,
    dob
) VALUES (
             'John',
             'Doe',
             'John Doe',
             'john.doe@example.com',
             '$2a$10$abcdefghijABCDEFGHIJ1234567890abcdefghijABCDEFGHIJ', -- sample bcrypt password
             'USA',
             '1234567890',
             '123456789',
             '1990-05-15'
         );
