package com.udescbittorrent.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.udescbittorrent.dtos.LogDto;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvDatabaseService {
    private static CsvDatabaseService instance;
    private CsvDatabaseService(){}
    public static CsvDatabaseService get() {
        if (instance == null) {
            instance = new CsvDatabaseService();
        }
        return instance;
    }
    String fileName = "trackerLogs.csv";
    String path = "logs/";
    String filePath = path + fileName;
    String[] headers = new String[]{
        "Ip_Remetente",
        "Nome_Remetente",
        "Ip_Destinatário",
        "Nome_Destinatário",
        "Tipo_Mensagem",
        "Conteudo_Mensagem",
        "DataHora"
    };

    public void addRow(LogDto logDto) throws IOException, CsvValidationException {
        List<String[]> rows = getRows();
        rows = checkHeaders(rows);
        rows.add(new String[]{
            logDto.getSenderIp(),
            logDto.getSenderName(),
            logDto.getRecipientIp(),
            logDto.getRecipientIp(),
            logDto.getMessageType(),
            logDto.getMessageContent(),
            logDto.getDateTime()
        });
        writeRows(rows);
    }

    private void writeRows(List<String[]> rows) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(rows);
        }
    }

    private List<String[]> getRows() throws CsvValidationException, IOException {
        List<String[]> rows = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                rows.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não existe!");
            File fPath = new File(path);
            if(!fPath.exists()) fPath.mkdirs();
        }
        return rows;
    }

    private List<String[]> checkHeaders(List<String[]> rows) {
        if(rows.isEmpty() || !Arrays.equals(rows.get(0),headers)){
            List<String[]> newRows = new ArrayList<>();
            newRows.add(headers);
            return newRows;
        }
        return rows;
    }
}
