package com.udescbittorrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeerDto {
    private String port;
    private String userName;
    private String address;

    @Override
    public String toString() {
        return String.format("%s %s:%s", userName,address,port);
    }
}
