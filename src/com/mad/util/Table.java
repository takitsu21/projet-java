package com.mad.util;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Table {
    private StringBuilder csv = new StringBuilder();
    public JTable table;
    public JScrollPane Jscroll;

    public String getCsv() {
        return csv.toString();
    }

    public void setCsv(String csv) {
        this.csv = new StringBuilder(csv);
    }

    public static String[][] sDataToArray(String data) {
        String[] ligne;
        ligne = data.split("\"\n\"");
        String[][] tableau = new String[ligne.length][];

        for (int i = 0; i < ligne.length; i++) {
            tableau[i] = ligne[i].split("\",\"");
        }

        tableau[0][0] = tableau[0][0].replace("\"", "");
        tableau[tableau.length - 1][tableau[0].length - 1] =
                tableau[tableau.length - 1][tableau[0].length - 1].replace("\"", "");
        return tableau;
    }
    
    public void TableXML(String path, String data) throws IOException, SAXException, ParserConfigurationException {
        XML2CSV xmlConverter = new XML2CSV(path);
        xmlConverter.convert();
        setCsv(data);
        String[][] tableau = sDataToArray(data);
        TableModel tm = new DefaultTableModel(Arrays.copyOfRange(tableau, 1, tableau.length), tableau[0]);
        table = new JTable(tm);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Jscroll = new JScrollPane(table);
    }


    public void TableCSV(String path) throws IOException {
        FileReader fr = new FileReader(path);
        FileReader fr_count = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        BufferedReader br_count = new BufferedReader(fr_count);

        String ln = br_count.readLine();
        int nbline = 1;
        while (ln != null) {
            nbline++;
            ln = br_count.readLine();
        }
        br_count.close();
        String line = br.readLine();
        csv.append(line)
                .append('\n');

        String[] column = line.split("\",\"");
        column[0] = column[0].replace("\"", "");

        String[][] data = new String[nbline - 2][column.length];
        int i = 0;
        line = br.readLine();

        String[] temp = line.split("\",\"");
        while (line != null) {
            csv.append(line)
                    .append('\n');
            System.arraycopy(temp, 0, data[i], 0, temp.length);
            data[i][temp.length - 1] = data[i][temp.length - 1].replace("\"", "");
            data[i][0] = data[i][0].replace("\"", "");
            line = br.readLine();
            if (line != null) {
                temp = line.split("\",\"");
            }
            if (i < nbline - 1) {
                i++;
            }

        }
        table = new JTable(data, column);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        Jscroll = new JScrollPane(table);
    }
}