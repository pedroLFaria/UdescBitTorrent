package com.udescbittorrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MessageDto {
    String destination;
    String message;
}