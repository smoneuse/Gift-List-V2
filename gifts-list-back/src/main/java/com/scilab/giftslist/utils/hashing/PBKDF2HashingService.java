package com.scilab.giftslist.utils.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.stereotype.Service;

@Service
public class PBKDF2HashingService implements HashingService{
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final int SALT_BYTE_SIZE = 24;
    private static final int KEY_LENGTH=192;
    private static final int PBKDF2_ITERATIONS = 1000;

    PBKDF2HashingService() {
    }

    public Hash createHash(String toHash) {
        return this.createHash(toHash.toCharArray());
    }

    public Hash createHash(char[] toHash) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        byte[] hash = this.pbkdf2(toHash, salt);
        return new Hash(hash, salt);
    }

    public boolean validatePassword(String password, Hash correctHash) {
        return this.validatePassword(password.toCharArray(), correctHash);
    }

    public boolean validatePassword(char[] password, Hash correctHash) {
        byte[] testHash = this.pbkdf2(password, correctHash.getSalt());
        return MessageDigest.isEqual(correctHash.getHash(), testHash);
    }

    private byte[] pbkdf2(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, PBKDF2_ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Unexpected exception while using crypting tool", ex);
        }
    }
}
