package com.inspier.carstyle;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

public class UploadPhotoTask extends AsyncTask<String, Void, Void> {

    private String IP_ADDRESS = "pdbx12.cafe24.com";
    private String ID = "pdbx12";
    private String PW = "ajtjs129^@^";
    private String UserID = null;
    private String fileName = null;
    private FTPClient ftpClient = null;

    private File file;

    public UploadPhotoTask(File file) {
        this.file = file;
        ftpClient = new FTPClient();
    }

    @Override
    protected Void doInBackground(String... params) {
        UserID = params[0];
        fileName = params[1];

        try {
            ftpClient.connect(IP_ADDRESS, 21);
            ftpClient.login(ID, PW);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(50 * 1024 * 1024);
            ftpClient.enterLocalPassiveMode();
            int reply = ftpClient.getReplyCode();

            //문제 없으면 업로드 진행
            if(!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
            }
            else {
                if(file.isFile()) {
                    FileInputStream ifile = new FileInputStream(file);
                    ftpClient.cwd("img");
                    ftpClient.makeDirectory(UserID);
                    ftpClient.cwd(UserID);
                    ftpClient.rest(file.getName());
                    ftpClient.appendFile(file.getName(), ifile);
                    ftpClient.rename(file.getName(), fileName);
                    Log.e("FTP", "file rename " + file.getName());
                }
            }
            ftpClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
