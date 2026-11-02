import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.JOptionPane;

public class Main implements Iterable<Item>{
    String currentItemsOverskrift = "name;date";
    ArrayList<Item> currentItems = new ArrayList<>();
    GUI gui = new GUI();

    // Lagres i en egen mappe i brukerens hjemmemappe, f.eks.
    // Windows: C:\Users\<navn>\Matskanning\currentItems.txt
    // Mac:     /Users/<navn>/Matskanning/currentItems.txt
    Path dataFil = Path.of(System.getProperty("user.home"), "Matskanning", "currentItems.txt");

    public Main(){
        importCurrentItems();
    }

    public void init(){
        gui.init(this);
    }

    private void importCurrentItems(){
        try{
            // Første gang: ta med eventuelle varer fra den gamle filen i programmappen
            Path gammelFil = Path.of("currentItems.txt");
            if(!Files.exists(dataFil) && Files.exists(gammelFil)){
                Files.createDirectories(dataFil.getParent());
                Files.copy(gammelFil, dataFil);
            }

            if(!Files.exists(dataFil)){
                return; // ingen varer lagret ennå
            }

            List<String> linjer = Files.readAllLines(dataFil, StandardCharsets.UTF_8);
            for(String linje : linjer){
                if(linje.isBlank() || linje.startsWith(currentItemsOverskrift)){
                    continue;
                }
                String[] biter = linje.split(";");
                currentItems.add(new Item(biter[0], biter[1]));
            }
        } catch(Exception e){
            visFeil("Kunne ikke lese lagrede varer:\n" + e.getMessage());
        }

        currentItems.sort(null);
    }

    private void updateCurrentItems(){
        currentItems.sort(null);
        StringBuilder innhold = new StringBuilder(currentItemsOverskrift);
        for(Item item : currentItems){
            innhold.append("\n").append(item.getName()).append(";").append(item.getDate().toString());
        }

        try{
            Files.createDirectories(dataFil.getParent());
            Files.writeString(dataFil, innhold.toString(), StandardCharsets.UTF_8);
        } catch(IOException e){
            visFeil("Kunne ikke lagre varene:\n" + e.getMessage());
        }
    }

    private void visFeil(String melding){
        JOptionPane.showMessageDialog(null, melding, "Feil", JOptionPane.ERROR_MESSAGE);
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