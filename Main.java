import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;

public class Main implements Iterable<Item>{
    String currentItemsOverskrift = "name;date";
    ArrayList<Item> currentItems = new ArrayList<>();
    GUI gui = new GUI();

    public Main(){
        importCurrentItems();
    }

    public void init(){
        gui.init(this);
    }

    private void importCurrentItems(){
        Scanner fil = null;
        try{
            fil = new Scanner(new File("currentItems.txt"));
        } catch(FileNotFoundException e){
            System.exit(1);
        }

        while(fil.hasNext()){
            String linje = fil.nextLine();
            if(linje.startsWith(currentItemsOverskrift)){
                continue;
            }

            String[] biter = linje.split(";");
            Item item = new Item(biter[0],biter[1]);
            currentItems.add(item);
        }

        fil.close();

        currentItems.sort(null);
    }

    private void updateCurrentItems(){
        currentItems.sort(null);
        PrintWriter fil = null;
        try{
            fil = new PrintWriter("currentItems.txt");
        } catch(FileNotFoundException e){
            System.exit(1);
        }

        fil.print(currentItemsOverskrift);

        for(Item item : currentItems){
            fil.print("\n"+item.getName()+";"+item.getDate().toString());
        }

        fil.close();
    }

    public void addItem(Item item){
        currentItems.add(item);
        updateCurrentItems();
    }

    public void removeItem(Item item){
        currentItems.remove(item);
        updateCurrentItems();
    }

    @Override
    public Iterator<Item> iterator(){
        return new MainIterator();
    }

    private class MainIterator implements Iterator<Item>{
        int teller=0;
        @Override
        public boolean hasNext(){
            return teller<currentItems.size();
        }

        @Override
        public Item next(){
            teller++;
            return currentItems.get(teller-1);
        }
    }
}
