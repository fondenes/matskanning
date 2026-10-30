import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI {
    Main main;
    JFrame vindu = new JFrame("Varer");
    JPanel hovedPanel = new JPanel();
    JPanel addPanel = new JPanel();
    JPanel displayPanel = new JPanel();

    private class RemoveHendelse implements ActionListener{
        @Override 
        public void actionPerformed(ActionEvent ae){
            RemoveKnapp knapp = (RemoveKnapp)ae.getSource();
            main.removeItem(knapp.item);
            displayItems();
        }
    }

    private class AddHendelse implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            JTextField nameField = new JTextField();
            JTextField dateField = new JTextField();

            JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            inputPanel.add(new JLabel("Navn:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Dato (åååå-mm-dd):"));
            inputPanel.add(dateField);

            int result = JOptionPane.showConfirmDialog(
                vindu,
                inputPanel,
                "Legg til vare",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if(result == JOptionPane.OK_OPTION){
                try{
                    Item nyVare = new Item(nameField.getText(), dateField.getText());
                    main.addItem(nyVare); // forutsetter at Main har en slik metode
                    displayItems();
                } catch(Exception e){
                    JOptionPane.showMessageDialog(
                        vindu,
                        "Ugyldig input. Sjekk at dato er på formatet åååå-mm-dd.",
                        "Feil",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }

    private class RemoveKnapp extends JButton{
        Item item;
        public RemoveKnapp(Item item){
            this.item = item;
            setPreferredSize(new Dimension(15,15));
            setMargin(new Insets(0,10,0,10));
        }
    }

    private class ItemPanel extends JPanel{
        Item item;
        JButton removeButton;
        JLabel daysUntilExp = new JLabel();
        JLabel name = new JLabel();
        JLabel date = new JLabel();

        public ItemPanel(Item item){
            this.item = item;
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setLayout(new FlowLayout(FlowLayout.LEFT));
            removeButton = new RemoveKnapp(item);
            this.add(removeButton);
            this.add(daysUntilExp);
            this.add(name);
            this.add(date);
            removeButton.addActionListener(new RemoveHendelse());
            name.setText(item.getName()+", ");
            date.setText(item.getDate().toString());
            update();

            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 6, 0),          // gap under kortet
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),     // selve borderen
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)      // padding inni
                )
            ));

            setMaximumSize(new Dimension(Integer.MAX_VALUE, getPreferredSize().height));
        }

        public void update(){
            long daysLeft = item.daysUntilExpiration();
            daysUntilExp.setText(daysLeft+" dager, ");
            if(item.expired()){
                daysUntilExp.setForeground(Color.RED);
            } else if(daysLeft==0){
                daysUntilExp.setForeground(Color.ORANGE);
            } else if(daysLeft<3){
                daysUntilExp.setForeground(Color.YELLOW);
            }
        }
    }

    public void init(Main input){
        main = input;
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch(Exception e){
            System.exit(1);
        }

        vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vindu.add(hovedPanel);

        JLabel overskrift = new JLabel("Matvarer:");
        overskrift.setFont(new Font("Arial", Font.BOLD, 24));
        overskrift.setHorizontalAlignment(SwingConstants.CENTER);
        overskrift.setAlignmentX(Component.CENTER_ALIGNMENT);
        overskrift.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        overskrift.setMaximumSize(new Dimension(Integer.MAX_VALUE, overskrift.getPreferredSize().height));
        hovedPanel.add(overskrift);

        hovedPanel.add(addPanel);
        addPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, addPanel.getPreferredSize().height));
        JButton addKnapp = new JButton("Legg til");
        addKnapp.setBackground(Color.GREEN);
        addKnapp.setForeground(Color.WHITE);
        addKnapp.addActionListener(new AddHendelse());
        addPanel.add(addKnapp);

        JScrollPane displayScrollPane = new JScrollPane(displayPanel);
        displayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        displayScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        hovedPanel.add(displayScrollPane);

        hovedPanel.setLayout(new BoxLayout(hovedPanel,BoxLayout.Y_AXIS));
        displayPanel.setLayout(new BoxLayout(displayPanel,BoxLayout.Y_AXIS));

        displayItems();

        vindu.setSize(375, 667);
        vindu.setResizable(false);
        vindu.setLocationRelativeTo(null);
        vindu.setVisible(true);
    }

    public void displayItems(){
        displayPanel.removeAll();
        for(Item item : main){
            ItemPanel itemPanel = new ItemPanel(item);
            displayPanel.add(itemPanel);
        }
        vindu.revalidate();
        vindu.repaint();
    }
}