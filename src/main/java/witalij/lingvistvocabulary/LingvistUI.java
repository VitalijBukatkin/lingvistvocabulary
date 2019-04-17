package witalij.lingvistvocabulary;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;

public class LingvistUI extends JFrame {
    private LingvistParser lingvistParser = LingvistParser.getInstance();
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    private JList<String> words = new JList<>();
    private JCheckBox duplicates = new JCheckBox("Remove duplicates", true);

    private Vector<String> data;

    public static void main(String[] args) {
        new LingvistUI();
    }

    private LingvistUI() throws HeadlessException {
        super("Linguist Parser");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        getContentPane().add(panel);

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));
        panel.add(menu);

        JButton choose = new JButton("Choose");
        choose.addActionListener((a)-> choosing());
        choose.setSize(0, 10);
        menu.add(choose);

        JButton copy = new JButton("Copy");
        copy.addActionListener((a) -> copying());
        copy.setSize(50, 10);
        menu.add(copy);

        menu.add(duplicates);

        words.setPrototypeCellValue("12345");
        panel.add(new JScrollPane(words));

        setSize(400, 500);
        setVisible(true);
    }

    private void copying(){
        if(data != null){
            String string = String.join("\n", data);
            StringSelection selection= new StringSelection(string);
            clipboard.setContents(selection, selection);
            JOptionPane.showMessageDialog(this, "Copy to clipboard!");
        }
    }

    private void choosing(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(chooser.showDialog(null, "Choose directory") == JFileChooser.APPROVE_OPTION){
            try {
                data = (Vector<String>) lingvistParser.extract(chooser.getSelectedFile());
                if(duplicates.isSelected()) {
                    removeDuplicates();
                }
                words.setListData(data);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this, "Can't load!");
                e.printStackTrace();
            }
        }
    }

    private void removeDuplicates(){
        Set<String> set = new LinkedHashSet<>(data);
        data.clear();
        data.addAll(set);
    }
}