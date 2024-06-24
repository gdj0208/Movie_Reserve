public class Schedule_data {
	
	public int movie_id;
	public String movie_name;
	public String cinema;
	public String day;
	public int schedule_id;
	
	Schedule_data(int movie_id, String movie_name, String cinema, String day, int schedule_id) {
		this.movie_id = movie_id;
		this.movie_name = movie_name;
		this.cinema = cinema;
		this.day = day;
		this.schedule_id = schedule_id;
	}
}
