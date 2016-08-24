package com.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
 
/**
 * A utility class that encrypts or decrypts a file.
 * @author www.codejava.net
 *
 */
public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    private static final  String KEY = "Wm#ruOp&12V_x#hE";
    private static final String ENCRYPTED_FILE_EXT = ".enc";
    private static enum INPUT_FILE{KEEP,REMOVE};
    
    public static void encrypt(File inputFile, File outputFile,INPUT_FILE state)
            throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, KEY, inputFile, outputFile);
        if(state.equals(INPUT_FILE.REMOVE)){
        	boolean status = true;
        	try{
        		status = inputFile.delete();
        		System.out.println(status);
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
    }
    
    public static void encrypt(File inputFile, File outputFile)
            throws CryptoException {
    	encrypt(inputFile, outputFile,INPUT_FILE.REMOVE);
    }
    
    public static void encrypt(String inputFilePath)
            throws CryptoException {
    	File inputFile = new File(inputFilePath);
    	File outputFile = new File(inputFilePath+ENCRYPTED_FILE_EXT);
    	encrypt(inputFile, outputFile,INPUT_FILE.REMOVE);
    }
 
    public static boolean decrypt(String encryptedFile) throws CryptoException{
    	File encFile = new File(encryptedFile+ENCRYPTED_FILE_EXT);
    	File file = new File(encryptedFile);
    	if(encFile.exists()){
    		decrypt(encFile,file);
    		return true;
    	}else{
    		return false;
    	}
    	
    }
    
    
    public static void decrypt(File inputFile, File outputFile)
            throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, KEY, inputFile, outputFile);
        
    }
 
    private static void doCrypto(int cipherMode, String key, File inputFile,
            File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);
             
            FileInputStream inputStream = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);
             
            byte[] outputBytes = cipher.doFinal(inputBytes);
             
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();
             
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }
    
    public static void main(String[] args) {
    	
    	String fileName = "E:\\Software\\office\\apache-tomcat-8.0.32\\storage\\191\\feesReceipt\\100.html";
    	File inputFile = new File(fileName);
        File encryptedFile = new File(fileName+".encrypted");
        try {
            //CryptoUtils.encrypt(inputFile, encryptedFile);
            CryptoUtils.decrypt(fileName);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
	}
}