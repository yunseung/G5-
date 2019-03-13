/**
 * <pre>
 * ��ó : http://arabiannight.tistory.com/64 [�ƶ��� ����Ʈ]
 * �ڵ��� Rooting ���� üũ ����
 * </pre>
 * 
 * co.kr.common RootingCheck.java
 * 
 * @since 2013.2013. 10. 4.
 * @version v1.0.0
 * @author Calix
 */
package com.ktrental.activity;

import java.io.File;

public class RootingCheck
{
    /* ******************** �� ���� ���� ���� ********************************* */
//  @off
    /** Rooting ���� üũ PATH  */
    private static String[] RootFilesPath = new String[] {
            "system/bin/su", "system/xbin/su", "system/xbin/who", 
            "system/xbin/whoami", "system/app/SuperUser.apk", 
            "data/data/com.noshufou.android.su" 
            };
//  @on
    /* ******************** �� ���� ���� �� ********************************* */

    /* ******************** �������̽� ���� ���� ********************************* */

    /* ******************** �������̽� ���� �� ********************************* */

    /* ******************** �޼ҵ� ���� ���� ********************************* */

    /**
     * <pre>
     * ���� ���θ� üũ�Ѵ�.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @return <pre>
     * [true]   : ������ ����
     * [false]  : ���þ��� ����
     * </pre>
     */
    public boolean isRootingCheck()
    {
        boolean isRootingFlag = false;

        try
        {
            Runtime.getRuntime().exec("su");
            isRootingFlag = true;
        }
        catch (Exception e)
        {
            // Exception ���� ���� false;
            isRootingFlag = false;
        }

        if (!isRootingFlag)
        {
            isRootingFlag = checkRootingFiles(createFiles(RootFilesPath));
        }

        return isRootingFlag;
    }

    /**
     * <pre>
     * �������� �ǽ� Path�� ���� ���ϵ��� �� �Ѵ�.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param sfiles
     * @return <pre></pre>
     */
    private File[] createFiles(String[] sfiles)
    {
        File[] rootingFiles = new File[sfiles.length];
        for (int i = 0; i < sfiles.length; i++)
        {
            rootingFiles[i] = new File(sfiles[i]);
        }
        return rootingFiles;
    }

    /**
     * <pre>
     * �������� ���θ� Ȯ�� �Ѵ�.
     * </pre>
     * 
     * @since 2013. 10. 4.
     * @version v1.0.0
     * @author Calix
     * @param file
     * @return <pre>
     * [true]   : ������ ����
     * [false]  : ���þ��� ����
     * </pre>
     */
    private boolean checkRootingFiles(File... file)
    {
        boolean result = false;
        for (File f : file)
        {
            if (f != null && f.isFile())
            {
                result = true;
                break;
            }
            else
            {
                result = false;
            }
        }
        return result;
    }
    /* ******************** �޼ҵ� ���� �� ********************************* */

    /* ******************** Listener ���� ���� ********************************* */

    /* ******************** Listener ���� �� ********************************* */
}
