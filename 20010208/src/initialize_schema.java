import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class initialize_schema {
	private static Statement stmt = connection.stmt;
    public static String main() {

        try {
      
        	// Drop and create database
            stmt.executeUpdate("DROP DATABASE IF EXISTS db1");
            stmt.executeUpdate("CREATE DATABASE db1");

            // Set foreign key checks off
            stmt.executeUpdate("SET foreign_key_checks = 0");

            // Create tables
            String createMovieTable = "CREATE TABLE db1.movie (" +
                "movie_id INTEGER NOT NULL, " +
                "movie_name VARCHAR(50) NOT NULL, " +
                "movie_runtime TIME NOT NULL, " +
                "movie_rank INTEGER NOT NULL, " +
                "movie_director VARCHAR(50) NOT NULL, " +
                "movie_actor VARCHAR(255) NOT NULL, " +
                "movie_genre VARCHAR(50) NOT NULL, " +
                "movie_introduce VARCHAR(255) NOT NULL, " +
                "movie_release_date DATE NOT NULL, " +
                "movie_grade INTEGER NOT NULL, " +
                "PRIMARY KEY (movie_id)" +
                ")";
            stmt.executeUpdate(createMovieTable);

            String createCinemaTable = "CREATE TABLE db1.cinema (" +
                "cinema_id INTEGER NOT NULL, " +
                "cinema_seat INTEGER, " +
                "cinema_status BOOLEAN, " +
                "cinema_seat_x INTEGER, " +
                "cinema_seat_y INTEGER, " +
                "PRIMARY KEY (cinema_id)" +
                ")";
            stmt.executeUpdate(createCinemaTable);

            String createScheduleTable = "CREATE TABLE db1.schedule (" +
                "schedule_id INTEGER NOT NULL, " +
                "schedule_movie INTEGER, " +
                "schedule_cinema INTEGER, " +
                "schedule_release DATE, " +
                "schedule_day VARCHAR(50), " +
                "schedule_count INTEGER, " +
                "schedule_genre VARCHAR(50), " +
                "schedule_start INTEGER, " +
                "PRIMARY KEY (schedule_id), " +
                "FOREIGN KEY(schedule_movie) REFERENCES db1.movie(movie_id), " +
                "FOREIGN KEY(schedule_cinema) REFERENCES db1.cinema(cinema_id)" +
                ")";
            stmt.executeUpdate(createScheduleTable);

            String createSeatTable = "CREATE TABLE db1.seat (" +
                "seat_id INTEGER NOT NULL, " +
                "seat_num INTEGER NOT NULL, " +
                "seat_cinema INTEGER, " +
                "seat_status BOOLEAN, " +
                "PRIMARY KEY (seat_id), " +
                "FOREIGN KEY(seat_cinema) REFERENCES db1.cinema(cinema_id)" +
                ")";
            stmt.executeUpdate(createSeatTable);

            String createUserTable = "CREATE TABLE db1.user (" +
                "user_id INTEGER NOT NULL, " +
                "user_name VARCHAR(50), " +
                "user_password VARCHAR(50)," +
                "user_phone VARCHAR(50), " +
                "user_email VARCHAR(50), " +
                "user_manager BOOLEAN, " +
                "PRIMARY KEY (user_id)" +
                ")";
            stmt.executeUpdate(createUserTable);

            String createBookingTable = "CREATE TABLE db1.booking (" +
                "booking_id INTEGER NOT NULL, " +
                "booking_pay_stretegy VARCHAR(50), " +
                "booking_pay_status BOOLEAN, " +
                "booking_pay_amount INTEGER, " +
                "booking_user INTEGER, " +
                "booking_date DATE, " +
                "PRIMARY KEY (booking_id), " +
                "FOREIGN KEY(booking_user) REFERENCES db1.user(user_id)" +
                ")";
            stmt.executeUpdate(createBookingTable);

            String createTicketTable = "CREATE TABLE db1.ticket (" +
                "ticket_id INTEGER NOT NULL, " +
                "ticket_schedule INTEGER, " +
                "ticket_cinema INTEGER, " +
                "ticket_seat INTEGER, " +
                "ticket_booking INTEGER, " +
                "ticket_status BOOLEAN, " +
                "ticket_price_std INTEGER, " +
                "ticket_price_sell INTEGER, " +
                "PRIMARY KEY (ticket_id), " +
                "FOREIGN KEY(ticket_schedule) REFERENCES db1.schedule(schedule_id), " +
                "FOREIGN KEY(ticket_cinema) REFERENCES db1.cinema(cinema_id), " +
                "FOREIGN KEY(ticket_seat) REFERENCES db1.seat(seat_id), " +
                "FOREIGN KEY(ticket_booking) REFERENCES db1.booking(booking_id)" +
                ")";
            stmt.executeUpdate(createTicketTable);

            // Set foreign key checks on
            stmt.executeUpdate("SET foreign_key_checks = 1");

            // Insert data into tables
            String insertMovies = "INSERT INTO db1.movie (movie_id, movie_name, movie_runtime, movie_rank, movie_director, movie_actor, movie_genre, movie_introduce, movie_release_date, movie_grade) VALUES " +
                "(1, 'Inception', '02:28:00', 9, 'Christopher Nolan', 'Leonardo DiCaprio, Joseph Gordon-Levitt', 'Action, Sci-Fi', 'A thief who enters the dreams of others to steal secrets from their subconscious.', '2023-01-01', 10)," +
                "(2, 'The Shawshank Redemption', '02:22:00', 10, 'Frank Darabont', 'Tim Robbins, Morgan Freeman', 'Drama', 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', '2023-02-01', 10)," +
                "(3, 'The Godfather', '02:55:00', 10, 'Francis Ford Coppola', 'Marlon Brando, Al Pacino', 'Crime, Drama', 'An organized crime dynasty''s aging patriarch transfers control of his clandestine empire to his reluctant son.', '2023-03-01', 10)," +
                "(4, 'The Dark Knight', '02:32:00', 9, 'Christopher Nolan', 'Christian Bale, Heath Ledger', 'Action, Crime, Drama', 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.', '2023-04-01', 9)," +
                "(5, 'Pulp Fiction', '02:34:00', 9, 'Quentin Tarantino', 'John Travolta, Uma Thurman', 'Crime, Drama', 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', '2023-05-01', 9)," +
                "(6, 'Fight Club', '02:19:00', 8, 'David Fincher', 'Brad Pitt, Edward Norton', 'Drama', 'An insomniac office worker and a devil-may-care soap maker form an underground fight club that evolves into much more.', '2023-06-01', 8)," +
                "(7, 'Forrest Gump', '02:22:00', 9, 'Robert Zemeckis', 'Tom Hanks, Robin Wright', 'Drama, Romance', 'The presidencies of Kennedy and Johnson, the Vietnam War, the Watergate scandal and other historical events unfold from the perspective of an Alabama man with an IQ of 75.', '2023-07-01', 9)," +
                "(8, 'The Matrix', '02:16:00', 8, 'Lana Wachowski, Lilly Wachowski', 'Keanu Reeves, Laurence Fishburne', 'Action, Sci-Fi', 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.', '2023-08-01', 9)," +
                "(9, 'The Silence of the Lambs', '01:58:00', 8, 'Jonathan Demme', 'Jodie Foster, Anthony Hopkins', 'Crime, Drama, Thriller', 'A young F.B.I. cadet must confide in an incarcerated and manipulative killer to receive his help on catching another serial killer who skins his victims.', '2023-09-01', 9)," +
                "(10, 'Interstellar', '02:49:00', 9, 'Christopher Nolan', 'Matthew McConaughey, Anne Hathaway', 'Adventure, Drama, Sci-Fi', 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity''s survival.', '2023-10-01', 10)," +
                "(11, 'Gladiator', '02:35:00', 8, 'Ridley Scott', 'Russell Crowe, Joaquin Phoenix', 'Action, Adventure, Drama', 'A former Roman General sets out to exact vengeance against the corrupt emperor who murdered his family and sent him into slavery.', '2023-11-01', 8)," +
                "(12, 'Se7en', '02:07:00', 8, 'David Fincher', 'Morgan Freeman, Brad Pitt', 'Crime, Drama, Mystery', 'Two detectives, a rookie and a veteran, hunt a serial killer who uses the seven deadly sins as his motives.', '2023-12-01', 8)";
            stmt.executeUpdate(insertMovies);

            String insertUsers = "INSERT INTO db1.user (user_id, user_name,user_password, user_phone, user_email, user_manager) VALUES " +
                "(1, 'root','1234', '010-1234-1234', 'abc@naver.com', 1)," +
                "(2, 'user1','user1', '010-2345-2345', 'def@naver.com', 0)," +
                "(3, 'user2','user2', '010-3456-3456', 'ghi@naver.com', 0)," +
                "(4, 'user3','user3', '010-4567-4567', 'jkl@naver.com', 0)," +
                "(5, 'user4','user4', '010-5678-5678', 'mno@naver.com', 0)," +
                "(6, 'user5','user5', '010-6789-6789', 'pqr@naver.com', 0)," +
                "(7, 'user6','user6', '010-7890-7890', 'stu@naver.com', 0)," +
                "(8, 'user7','user7', '010-8901-8901', 'vwx@naver.com', 0)," +
                "(9, 'user8','user8', '010-9012-9012', 'yz@naver.com', 0)," +
                "(10, 'user9','user9', '010-0123-0123', 'abc2@naver.com', 0)," +
                "(11, 'user10','user10', '010-1234-2345', 'def2@naver.com', 0)," +
                "(12, 'user11','user11', '010-2345-3456', 'ghi2@naver.com', 0)";
            stmt.executeUpdate(insertUsers);

            String insertCinemas = "INSERT INTO db1.cinema (cinema_id, cinema_seat, cinema_status, cinema_seat_x, cinema_seat_y) VALUES " +
                "(1, 3, TRUE, 10, 10)," +
                "(2, 3, TRUE, 15, 10)," +
                "(3, 3, TRUE, 10, 20)," +
                "(4, 3, TRUE, 12, 10)," +
                "(5, 3, TRUE, 15, 12)," +
                "(6, 3, TRUE, 14, 10)," +
                "(7, 3, TRUE, 16, 10)," +
                "(8, 3, TRUE, 11, 10)," +
                "(9, 3, TRUE, 13, 10)," +
                "(10, 1, TRUE, 17, 10)," +
                "(11, 1, TRUE, 19, 10)," +
                "(12, 1, TRUE, 21, 10)";
            stmt.executeUpdate(insertCinemas);

            String insertSchedules = "INSERT INTO db1.schedule (schedule_id, schedule_movie, schedule_cinema, schedule_release, schedule_day, schedule_count, schedule_genre, schedule_start) VALUES " +
                "(1, 1, 1, '2024-05-20', 'Monday', 3, 'Action', 14)," +
                "(2, 2, 2, '2024-05-21', 'Tuesday', 2, 'Drama', 16)," +
                "(3, 3, 3, '2024-05-22', 'Wednesday', 4, 'Crime', 15)," +
                "(4, 4, 4, '2024-05-23', 'Thursday', 3, 'Action', 17)," +
                "(5, 5, 5, '2024-05-24', 'Friday', 2, 'Drama', 18)," +
                "(6, 6, 6, '2024-05-25', 'Saturday', 3, 'Drama', 19)," +
                "(7, 7, 7, '2024-05-26', 'Sunday', 2, 'Romance', 20)," +
                "(8, 8, 8, '2024-05-27', 'Monday', 4, 'Action', 21)," +
                "(9, 9, 9, '2024-05-28', 'Tuesday', 3, 'Thriller', 22)," +
                "(10, 10, 10, '2024-05-29', 'Wednesday', 2, 'Adventure', 23)," +
                "(11, 11, 11, '2024-05-30', 'Thursday', 3, 'Sci-Fi', 24)," +
                "(12, 12, 12, '2024-05-31', 'Friday', 2, 'Mystery', 25)";
            stmt.executeUpdate(insertSchedules);

            String insertSeats = "INSERT INTO db1.seat (seat_id, seat_num, seat_cinema, seat_status) VALUES " +
                "(1, 1, 1, FALSE)," +
                "(2, 2, 1, TRUE)," +
                "(3, 3, 1, TRUE)," +
                "(4, 1, 2, FALSE)," +
                "(5, 2, 2, TRUE)," +
                "(6, 3, 2, TRUE)," +
                "(7, 1, 3, FALSE)," +
                "(8, 2, 3, TRUE)," +
                "(9, 3, 3, TRUE)," +
                "(10, 1, 4, FALSE)," +
                "(11, 2, 4, TRUE)," +
                "(12, 3, 4, TRUE)," +
                "(13, 1, 5, FALSE)," +
                "(14, 2, 5, TRUE)," +
                "(15, 3, 5, TRUE)," +
                "(16, 1, 6, FALSE)," +
                "(17, 2, 6, TRUE)," +
                "(18, 3, 6, TRUE)," +
                "(19, 1, 7, FALSE)," +
                "(20, 2, 7, TRUE)," +
                "(21, 3, 7, TRUE)," +
                "(22, 1, 8, FALSE)," +
                "(23, 2, 8, TRUE)," +
                "(24, 3, 8, TRUE)," +
                "(25, 1, 9, FALSE)," +
                "(26, 2, 9, TRUE)," +
                "(27, 3, 9, TRUE)," +
                "(28, 1, 10, FALSE)," +
                "(29, 2, 11, FALSE)," +
                "(30, 3, 12, FALSE)";
            stmt.executeUpdate(insertSeats);

            String insertBookings = "INSERT INTO db1.booking (booking_id, booking_pay_stretegy, booking_pay_status, booking_pay_amount, booking_user, booking_date) VALUES " +
                "(1, 'Credit Card', TRUE, 25000, 2, '2024-05-15')," +
                "(2, 'Cash', TRUE, 30000, 3, '2024-05-16')," +
                "(3, 'Debit Card', TRUE, 20000, 4, '2024-05-17')," +
                "(4, 'Credit Card', TRUE, 28000, 5, '2024-05-18')," +
                "(5, 'Cash', TRUE, 26000, 6, '2024-05-19')," +
                "(6, 'Debit Card', TRUE, 24000, 7, '2024-05-20')," +
                "(7, 'Credit Card', TRUE, 22000, 8, '2024-05-21')," +
                "(8, 'Cash', TRUE, 21000, 9, '2024-05-22')," +
                "(9, 'Debit Card', TRUE, 23000, 10, '2024-05-23')," +
                "(10, 'Credit Card', TRUE, 27000, 11, '2024-05-24')," +
                "(11, 'Cash', TRUE, 25000, 12, '2024-05-25')," +
                "(12, 'Debit Card', TRUE, 29000, 1, '2024-05-26')";
            stmt.executeUpdate(insertBookings);

            String insertTickets = "INSERT INTO db1.ticket (ticket_id, ticket_schedule, ticket_cinema, ticket_seat, ticket_booking, ticket_status, ticket_price_std, ticket_price_sell) VALUES " +
                "(1, 1, 1, 1, 1, TRUE, 15000, 20000)," +
                "(2, 2, 2, 4, 2, TRUE, 16000, 21000)," +
                "(3, 3, 3, 7, 3, TRUE, 17000, 22000)," +
                "(4, 4, 4, 10, 4, TRUE, 18000, 23000)," +
                "(5, 5, 5, 13, 5, TRUE, 19000, 24000)," +
                "(6, 6, 6, 16, 6, TRUE, 20000, 25000)," +
                "(7, 7, 7, 19, 7, TRUE, 21000, 26000)," +
                "(8, 8, 8, 22, 8, TRUE, 22000, 27000)," +
                "(9, 9, 9, 25, 9, TRUE, 23000, 28000)," +
                "(10, 10, 10, 28, 10, TRUE, 24000, 29000)," +
                "(11, 11, 11, 29, 11, TRUE, 25000, 30000)," +
                "(12, 12, 12, 30, 12, TRUE, 26000, 31000)";
            stmt.executeUpdate(insertTickets);
            
            System.out.println("DB 초기화 완료");


            return "DB 초기화 완료";
        } catch (SQLException e) {
            e.printStackTrace();
            return "DB 초기화 에러";
        } 
    }
}
