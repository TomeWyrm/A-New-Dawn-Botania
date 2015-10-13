package vazkii.botania.common.core.handler;

import cpw.mods.fml.common.ICrashCallable;

import java.util.List;

/**
 * Created by kotasc3 on 9/24/2015.
 */
public class CrashBotania implements ICrashCallable
{

    public CrashBotania()
    {
    }

    @Override
    public String getLabel ()
    {
        return "A-New-Dawn-Botania";
    }

    @Override
    public String call () throws Exception
    {
        String str = "THIS INSTANCE OF MINECRAFT IS USING A FORK OF BOTANIA NOT SANCTIONED BY VAZKII, DO NOT REPORT CRASHES UNLESS YOU PERSONALLY KNOW TOMEWYRM.";
        return str;
    }

}