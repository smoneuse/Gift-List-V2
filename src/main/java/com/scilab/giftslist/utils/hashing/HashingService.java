package com.scilab.giftslist.utils.hashing;

public interface HashingService {
    Hash createHash(String var1);

    Hash createHash(char[] var1);

    boolean validatePassword(char[] var1, Hash var2);

    boolean validatePassword(String var1, Hash var2);
}
