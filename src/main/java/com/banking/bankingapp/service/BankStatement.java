package com.banking.bankingapp.service;

import com.banking.bankingapp.dto.EmailDetails;
import com.banking.bankingapp.entity.TransactionEntity;
import com.banking.bankingapp.entity.UserEntity;
import com.banking.bankingapp.repositoty.TransactionRepository;
import com.banking.bankingapp.repositoty.UserRepository;
import com.banking.bankingapp.service.Imp.EmailServiceImp;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
    //retrieve list of transactions within a date range of an account
    //generate a pdf file of transaction
    //send the file via email

    private TransactionRepository transactionRepository;

    private UserRepository userRepository;

    private EmailServiceImp emailServiceImp;
    private static final String FILE = "//Users//giangtruong//Desktop//statement//MyStatement.pdf";

    public List<TransactionEntity> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {

        LocalDate startD = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate endD = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<TransactionEntity> transactionList = transactionRepository.findAll().stream()
                .filter(transactionEntity -> transactionEntity.getAccountNumber().equals(accountNumber))
                .filter(transactionEntity -> transactionEntity.getCreatedAt().isEqual(startD))
                .filter(transactionEntity -> transactionEntity.getCreatedAt().isEqual(endD)).toList();

        UserEntity user = userRepository.findByAccountNumber(accountNumber);
        String customername = user.getFullname();

        Rectangle  statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Demo Bank App"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Ho Chi Minh City, Vietnam"));
        bankName.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);
        PdfPCell customerName = new PdfPCell(new Phrase("Customer name: " + customername));
        customerName.setBorder(0);
        PdfPCell space = new PdfPCell();
        PdfPCell address = new PdfPCell(new Phrase("Address: " + user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        transactionList.forEach(transactionEntity -> {
            transactionTable.addCell(new Phrase(transactionEntity.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(transactionEntity.getTransactionType()));
            transactionTable.addCell(new Phrase(transactionEntity.getAmount().toString()));
            transactionTable.addCell(new Phrase(transactionEntity.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(customername);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setSubject("STATEMENT OF ACCOUNT");
        emailDetails.setMessageBody("Kindly find your requested account statement attached");
        emailDetails.setAttachment(FILE);

        emailServiceImp.sendEmailWithAttachment(emailDetails);

        return transactionList;
    }

}
