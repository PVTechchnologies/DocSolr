package com.docsolr.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.docsolr.dto.KeyGeneratorVo;
import com.docsolr.dto.UserVO;
import com.docsolr.entity.UserAuthority;
import com.docsolr.entity.Users;
import com.docsolr.entity.UserAuthority.Roles;
import com.docsolr.service.common.GenericService;
import com.docsolr.util.SecurityUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import sun.security.tools.keytool.CertAndKeyGen;
// import sun.security.tools.keytool.CertAndKeyGen; // Use this for Java 8 and above
import sun.security.x509.X500Name;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

@Controller
public class KeyToolController {
	
	@Autowired
	public GenericService<Users> userService;
	
	@Autowired
	GenericService<UserAuthority> userAuthGenericService;
	
	 	private static  int keysize = 1024;
	    private static  String commonName ;
	    private static  String organizationalUnit;
	    private static  String organization;
	    private static  String city ;
	    private static  String state;
	    private static  String country ;
	    private static  long validity = 1096; // 3 years
	    private static  String alias ;
	    private static  char[] keyPass = "nalle123".toCharArray();

	 @RequestMapping(value = "/generateKey", method = RequestMethod.POST)
	 @ResponseBody
	 public RedirectView generateKey(HttpServletRequest request, HttpServletResponse response,
			 	@ModelAttribute("newKeyGenerate")KeyGeneratorVo keyGeneratorVo,  ModelMap model ) {
			      
		 commonName=keyGeneratorVo.getCommonName();
		 organizationalUnit=keyGeneratorVo.getOrganizationalUnit();
		 organization=keyGeneratorVo.getOrganization();
		 city=keyGeneratorVo.getCity();
		 state=keyGeneratorVo.getState();
		 country=keyGeneratorVo.getCountry();
		 alias=keyGeneratorVo.getAlias();
		 
		try{
		 KeyStore keyStore = KeyStore.getInstance("JKS");
	        keyStore.load(null, null);

	        CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);

	        X500Name x500Name = new X500Name(commonName, organizationalUnit, organization, city, state, country);

	        keypair.generate(keysize);
	        PrivateKey privKey = keypair.getPrivateKey();

	        X509Certificate[] chain = new X509Certificate[1];

	        chain[0] = keypair.getSelfCertificate(x500Name, new Date(), (long) validity * 24 * 60 * 60);

	        keyStore.setKeyEntry(alias, privKey, keyPass, chain);

	        keyStore.store(new FileOutputStream("D:\\saml.jks"), keyPass);

	        System.out.println("path of keystore");
		 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		 
	/*	 model.addAttribute("name", user.getName());
		 Users user = new Users(userVo.getFirstName(), userVo.getLastName(), userVo.getEmail(), userVo.getPassword(), true,true);
		 UserAuthority userAuthority = new UserAuthority(Roles.ROLE_USER);
			Map authRestrictionMap = new HashMap();
			authRestrictionMap.put("authority", userAuthority.getAuthority());
			List<UserAuthority> userAuthorityList = userAuthGenericService.findLimitedEntity(UserAuthority.class, 0, authRestrictionMap, null);
			if(!userAuthorityList.isEmpty()){
				Set<UserAuthority> setOfAuthority = new HashSet<UserAuthority>();
				setOfAuthority.add(userAuthorityList.get(0));
				user.setAuthorities(setOfAuthority);
				user.setEnabled(true);
			}
			userService.saveEntity(user);
			SecurityUtil.signInUser(user);*/
			return new RedirectView("/user",true);
	 }
	 

	 private void newKeyToolCode() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException{
		 
		//CREATE A KEYSTORE OF TYPE "Java Key Store"
	    	KeyStore ks = KeyStore.getInstance("JKS");
	    	/*
	    	 * LOAD THE STORE
	    	 * The first time you're doing this (i.e. the keystore does not
	    	 * yet exist - you're creating it), you HAVE to load the keystore
	    	 * from a null source with null password. Before any methods can
	    	 * be called on your keystore you HAVE to load it first. Loading
	    	 * it from a null source and null password simply creates an empty
	    	 * keystore. At a later time, when you want to verify the keystore
	    	 * or get certificates (or whatever) you can load it from the
	    	 * file with your password.
	    	 */
	    	InputStream input = new FileInputStream(new File("D:\\saml.jks"));
	    	ks.load(input, keyPass );
	    	//GET THE FILE CONTAINING YOUR CERTIFICATE
	    	FileInputStream fis = new FileInputStream( "D:\\apollo.crt" );
	    	BufferedInputStream bis = new BufferedInputStream(fis);
	    	//I USE x.509 BECAUSE THAT'S WHAT keytool CREATES
	    	CertificateFactory cf = CertificateFactory.getInstance( "X.509" );
	    	//NOTE: THIS IS java.security.cert.Certificate NOT java.security.Certificate
	    	Certificate cert = null;
	    	/*
	    	 * I ONLY HAVE ONE CERT, I JUST USED "while" BECAUSE I'M JUST
	    	 * DOING TESTING AND WAS TAKING WHATEVER CODE I FOUND IN
	    	 * THE API DOCUMENTATION. I COULD HAVE DONE AN "if", BUT I
	    	 * WANTED TO SHOW HOW YOU WOULD HANDLE IT IF YOU GOT A CERT
	    	 * FROM VERISIGN THAT CONTAINED MULTIPLE CERTS
	    	 */
	    	//GET THE CERTS CONTAINED IN THIS ROOT CERT FILE
	    	while ( bis.available() > 0 )
	    	{
	    	 cert = cf.generateCertificate( bis );
	    	 ks.setCertificateEntry( "SGCert", cert );
	    	}
	    	//ADD TO THE KEYSTORE AND GIVE IT AN ALIAS NAME
	    	ks.setCertificateEntry( "SGCert", cert );
	    	//SAVE THE KEYSTORE TO A FILE
	    	/*
	    	 * After this is saved, I believe you can just do setCertificateEntry
	    	 * to add entries and then not call store. I believe it will update
	    	 * the existing store you load it from and not just in memory.
	    	 */
	    	ks.store( new FileOutputStream( "C:\\saml.jks" ), "MyPass".toCharArray() );
	    	Enumeration enumeration = ks.aliases();
	        while(enumeration.hasMoreElements()) {
	            String alias = (String)enumeration.nextElement();
	            System.out.println("alias name: " + alias);
	            Certificate certificate = ks.getCertificate(alias);
	            System.out.println(certificate.toString());

	        }
	 }
	 
}
