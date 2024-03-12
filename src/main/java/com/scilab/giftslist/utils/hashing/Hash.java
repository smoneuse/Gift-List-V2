package com.scilab.giftslist.utils.hashing;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class Hash {
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static final String HASH_SALT_SEPRARATOR = ":";
    private final byte[] hash;
    private final byte[] salt;

    public Hash(byte[] hash, byte[] salt) {
        this.hash = (byte[])((byte[]) Objects.requireNonNull(hash)).clone();
        this.salt = (byte[])((byte[])Objects.requireNonNull(salt)).clone();
    }

    public Hash(String hash, String salt) {
        this.hash = hexToBytes((String)Objects.requireNonNull(hash));
        this.salt = hexToBytes((String)Objects.requireNonNull(salt));
    }

    public Hash(String hashAndSalt){
        if(!hashAndSalt.contains(HASH_SALT_SEPRARATOR)){
            throw new IllegalArgumentException("Incorrect credentials hash and salt provided");
        }
        this.hash=hexToBytes(StringUtils.substringBefore(hashAndSalt, HASH_SALT_SEPRARATOR));
        this.salt=hexToBytes(StringUtils.substringAfter(hashAndSalt,HASH_SALT_SEPRARATOR));
    }

    public byte[] getHash() {
        return (byte[])this.hash.clone();
    }

    public byte[] getSalt() {
        return (byte[])this.salt.clone();
    }

    public String getHashAsString() {
        return bytesToHex(this.hash);
    }

    public String getSaltAsString() {
        return bytesToHex(this.salt);
    }

    public String toString() {
        return this.getHashAsString() + HASH_SALT_SEPRARATOR + this.getSaltAsString();
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for(int i = 0; i < bytes.length; ++i) {
            int v = bytes[i] & 255;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 15];
        }

        return new String(hexChars);
    }

    private static byte[] hexToBytes(String s) {
        if (s.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal string");
        } else {
            int len = s.length();
            byte[] data = new byte[len / 2];

            for(int i = 0; i < len; i += 2) {
                data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            }

            return data;
        }
    }
}
