import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item implements Comparable<Item>{
    private String name;

    private LocalDate date;
    

    public Item(String name, String date){
        this.name = name;

        String[] dateList = date.split("-");
        
        this.date = LocalDate.of(Integer.parseInt(dateList[0]),Integer.parseInt(dateList[1]),
        Integer.parseInt(dateList[2]));
    }

    public String getName(){
        return name;
    }

    public LocalDate getDate(){
        return date;
    }

    public boolean expired(){
        return daysUntilExpiration()<0;
    }

    public long daysUntilExpiration(){
        return ChronoUnit.DAYS.between(LocalDate.now(),date);
    }

    @Override
    public int compareTo(Item other){
        return date.compareTo(other.date);
    }

    @Override
    public String toString(){
        return name+", Exp: "+date.toString();
    }
}