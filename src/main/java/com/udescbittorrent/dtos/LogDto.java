package com.udescbittorrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogDto {
    String senderIp;
    String senderName;
    String recipientIp;
    String recipientName;
    String messageType;
    String messageContent;
    String dateTime;

    @Override
    public String toString() {
        return "LogDto{" +
            "senderIp='" + senderIp + '\'' +
            ", senderName='" + senderName + '\'' +
            ", recipientIp='" + recipientIp + '\'' +
            ", recipientName='" + recipientName + '\'' +
            ", messageType='" + messageType + '\'' +
            ", messageContent='" + messageContent + '\'' +
            ", dateTime='" + dateTime + '\'' +
            '}';
    }
}
