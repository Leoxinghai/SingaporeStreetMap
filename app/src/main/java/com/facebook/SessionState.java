// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;


public enum SessionState
{

	CREATED("CREATED", 0, Category.CREATED_CATEGORY),
	CREATED_TOKEN_LOADED("CREATED_TOKEN_LOADED", 1, Category.CREATED_CATEGORY),
	OPENING("OPENING", 2, Category.CREATED_CATEGORY),
	OPENED("OPENED", 3, Category.OPENED_CATEGORY),
	OPENED_TOKEN_UPDATED("OPENED_TOKEN_UPDATED", 4, Category.OPENED_CATEGORY),
	CLOSED_LOGIN_FAILED("CLOSED_LOGIN_FAILED", 5, Category.CLOSED_CATEGORY),
	CLOSED("CLOSED", 6, Category.CLOSED_CATEGORY);

    private static enum Category
    {

		CREATED_CATEGORY("CREATED_CATEGORY", 0),
		OPENED_CATEGORY("OPENED_CATEGORY", 1),
		CLOSED_CATEGORY("CLOSED_CATEGORY", 2);

        private Category(String s, int i)
        {

        }
    }

    Category category;

    private SessionState(String s, int i, Category category1)
    {
        category = category1;
    }


    public boolean isClosed()
    {
        return category == Category.CLOSED_CATEGORY;
    }

    public boolean isOpened()
    {
        return category == Category.OPENED_CATEGORY;
    }

}
