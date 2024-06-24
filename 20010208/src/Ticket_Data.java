public class Ticket_Data {

	String movie_name;
	String schedule_day;
	int schedule_cinema;
	int seat_id;
	int ticket_price;
	
	Ticket_Data(String Movie_Name,String day,int cinema,int seat_id,int price) {
		this.movie_name = Movie_Name;
		this.schedule_day = day;
		this.schedule_cinema = cinema;
		this.seat_id = seat_id;
		this.ticket_price = price;
	}
}
