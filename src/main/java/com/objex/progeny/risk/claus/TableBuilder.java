/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.progeny.risk.claus;

import com.google.common.collect.Lists;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.FIFTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.FOURTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.SEVENTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.SIXTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.THIRTY;
import static com.objex.progeny.risk.claus.ClausDelegate.AgeKey.TWENTY;
import static com.objex.progeny.risk.claus.ClausDelegate.twoFDRTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.motherAndMaternalAuntTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.singleMaternalSinglePaternalSDRTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.motherAndPaternalAuntTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.twoFDROvarianTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.singleFDROvarianTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.singleFDRTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.singleFDRandOvarianTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.singleSDRTbl;
import static com.objex.progeny.risk.claus.ClausDelegate.twoSDRTbl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jason
 */
public class TableBuilder {
    
    public static final Double REPORTING_INTERVAL = 10d;

   
    
    public static void buildTables() {
        
         //init single fdr
        add(singleFDRTbl,TWENTY,    .007 ,.005 ,.003 ,.002 ,.002 ,.001);
        add(singleFDRTbl,THIRTY,    .025 ,.017 ,.012 ,.008 ,.006 ,.005);
        add(singleFDRTbl,FOURTY,    .062 ,.044 ,.032 ,.023 ,.018 ,.015);
        add(singleFDRTbl,FIFTY,     .116 ,.086 ,.064 ,.049 ,.040 ,.035);
        add(singleFDRTbl,SIXTY,     .171 ,.130 ,.101 ,.082 ,.070 ,.062);
        add(singleFDRTbl,SEVENTY,   .211 ,.165 ,.132 ,.110 ,.096 ,.088); 
        
        
         //init single sdr
        add(singleSDRTbl,TWENTY,   .004 ,.003 ,.002 ,.001 ,.001 ,.001);
        add(singleSDRTbl,THIRTY,   .014 ,.010 ,.007 ,.006 ,.005 ,.004);
        add(singleSDRTbl,FOURTY,   .035 ,.027 ,.021 ,.017 ,.017 ,.013);
        add(singleSDRTbl,FIFTY,    .070 ,.056 ,.045 ,.038 ,.038 ,.032);
        add(singleSDRTbl,SIXTY,    .110 ,.090 ,.076 ,.067 ,.067 ,.058);
        add(singleSDRTbl,SEVENTY,  .142 ,.120 ,.104 ,.094 ,.094 ,.083); 
        
        //init single FDR Ovarian(repeated to conform to interface)  values to 80
        add(singleFDROvarianTbl,TWENTY,  .002 ,.002 ,.002 ,.002 ,.002 ,.002);
        add(singleFDROvarianTbl,THIRTY,  .010 ,.010 ,.010 ,.010 ,.010 ,.010);
        add(singleFDROvarianTbl,FOURTY,  .026 ,.026 ,.026 ,.026 ,.026 ,.026);
        add(singleFDROvarianTbl,FIFTY,   .054 ,.054 ,.054 ,.054 ,.054 ,.054);
        add(singleFDROvarianTbl,SIXTY,   .088 ,.088 ,.088 ,.088 ,.088 ,.088);
        add(singleFDROvarianTbl,SEVENTY, .117 ,.117 ,.117 ,.117 ,.117 ,.117); 
        
         //init multi FDR Ovarian(repeated to conform to interface)
        add(twoFDROvarianTbl,TWENTY,  .011 ,.011 ,.011 ,.011 ,.011 ,.011);
        add(twoFDROvarianTbl,THIRTY,  .037 ,.037 ,.037 ,.037 ,.037 ,.037);
        add(twoFDROvarianTbl,FOURTY,  .090 ,.090 ,.090 ,.090 ,.090 ,.090);
        add(twoFDROvarianTbl,FIFTY,   .164 ,.164 ,.164 ,.164 ,.164 ,.164);
        add(twoFDROvarianTbl,SIXTY,   .236 ,.236 ,.236 ,.236 ,.236 ,.236);
        add(twoFDROvarianTbl,SEVENTY, .285 ,.285 ,.285 ,.285 ,.285 ,.285); 
        
        //init single FDR and single FDR ovarian
        add(singleFDRandOvarianTbl,TWENTY,    .017 ,.015 ,.013 ,.010 ,.007 ,.005,.004);
        add(singleFDRandOvarianTbl,THIRTY,    .057 ,.050 ,.042 ,.034 ,.025 ,.019,.014);
        add(singleFDRandOvarianTbl,FOURTY,    .136 ,.122 ,.103 ,.082 ,.063 ,.048,.036);
        add(singleFDRandOvarianTbl,FIFTY,     .244 ,.219 ,.186 ,.151 ,.118 ,.091,.072);
        add(singleFDRandOvarianTbl,SIXTY,     .313 ,.309 ,.266 ,.218 ,.174 ,.138,.112);
        add(singleFDRandOvarianTbl,SEVENTY,   .406 ,.367 ,.319 ,.265 ,.214 ,.174,.144);
        //add(singleFDRandOvarianTbl,EIGHTY,    .434 ,.394 ,.344 ,.288 ,.236 ,.193,.163);add(singleFDROvarianTbl,TWENTY,  .002 ,.002 ,.002 ,.002 ,.002 ,.002);
       
 
        
         //init multiple fdr
        add(twoFDRTbl,TWENTY,  TWENTY  ,.021 ,.020 ,.018 ,.016 ,.014 ,.012);
        add(twoFDRTbl,THIRTY,  TWENTY  ,.069 ,.066 ,.061 ,.055 ,.048 ,.041);
        add(twoFDRTbl,FOURTY,  TWENTY  ,.166 ,.157 ,.146 ,.133 ,.117 ,.099);
        add(twoFDRTbl,FIFTY,   TWENTY  ,.295 ,.279 ,.261 ,.238 ,.210 ,.179);
        add(twoFDRTbl,SIXTY,   TWENTY  ,.412 ,.391 ,.366 ,.335 ,.297 ,.256);
        add(twoFDRTbl,SEVENTY, TWENTY  ,.484 ,.460 ,.434 ,.397 ,.354 ,.308); 
                               
        add(twoFDRTbl,TWENTY,  THIRTY  ,.018 ,.016 ,.014 ,.012 ,.009);
        add(twoFDRTbl,THIRTY,  THIRTY  ,.062 ,.056 ,.048 ,.040 ,.032);
        add(twoFDRTbl,FOURTY,  THIRTY  ,.148 ,.134 ,.116 ,.096 ,.077);
        add(twoFDRTbl,FIFTY,   THIRTY  ,.265 ,.239 ,.209 ,.175 ,.143);
        add(twoFDRTbl,SIXTY,   THIRTY  ,.371 ,.337 ,.296 ,.251 ,.207);
        add(twoFDRTbl,SEVENTY, THIRTY  ,.437 ,.399 ,.353 ,.302 ,.252);  
        
        add(twoFDRTbl,TWENTY,  FOURTY  ,.014 ,.012 ,.009 ,.007);
        add(twoFDRTbl,THIRTY,  FOURTY  ,.048 ,.039 ,.030 ,.023);
        add(twoFDRTbl,FOURTY,  FOURTY  ,.117 ,.096 ,.075 ,.058);
        add(twoFDRTbl,FIFTY,   FOURTY  ,.210 ,.174 ,.139 ,.108);
        add(twoFDRTbl,SIXTY,   FOURTY  ,.298 ,.249 ,.202 ,.161);      
        add(twoFDRTbl,SEVENTY, FOURTY  ,.354 ,.300 ,.246 ,.200);  

        add(twoFDRTbl,TWENTY,  FIFTY   ,.009 ,.006 ,.005);
        add(twoFDRTbl,THIRTY,  FIFTY   ,.030 ,.022 ,.016);
        add(twoFDRTbl,FOURTY,  FIFTY   ,.075 ,.056 ,.042);
        add(twoFDRTbl,FIFTY,   FIFTY   ,.138 ,.105 ,.081);
        add(twoFDRTbl,SIXTY,   FIFTY   ,.200 ,.157 ,.124);
        add(twoFDRTbl,SEVENTY, FIFTY   ,.245 ,.195 ,.158); 

        add(twoFDRTbl,TWENTY,  SIXTY   ,.004 ,.003);
        add(twoFDRTbl,THIRTY,  SIXTY   ,.016 ,.012);
        add(twoFDRTbl,FOURTY,  SIXTY   ,.041 ,.030);
        add(twoFDRTbl,FIFTY,   SIXTY   ,.080 ,.061);
        add(twoFDRTbl,SIXTY,   SIXTY   ,.122 ,.098);
        add(twoFDRTbl,SEVENTY, SIXTY   ,.156 ,.128);

        add(twoFDRTbl,TWENTY,  SEVENTY  ,.002);
        add(twoFDRTbl,THIRTY,  SEVENTY  ,.008);
        add(twoFDRTbl,FOURTY,  SEVENTY  ,.023);
        add(twoFDRTbl,FIFTY,   SEVENTY  ,.049);
        add(twoFDRTbl,SIXTY,   SEVENTY  ,.081);
        add(twoFDRTbl,SEVENTY, SEVENTY  ,.109);
        
         //init one paternal one maternal SDR tables
        add(singleMaternalSinglePaternalSDRTbl,TWENTY,  TWENTY  ,.007 ,.006 ,.005 ,.004 ,.004 ,.004);
        add(singleMaternalSinglePaternalSDRTbl,THIRTY,  TWENTY  ,.023 ,.020 ,.017 ,.016 ,.015 ,.014);
        add(singleMaternalSinglePaternalSDRTbl,FOURTY,  TWENTY  ,.057 ,.050 ,.044 ,.040 ,.038 ,.037);
        add(singleMaternalSinglePaternalSDRTbl,FIFTY,   TWENTY  ,.108 ,.095 ,.085 ,.079 ,.075 ,.073);
        add(singleMaternalSinglePaternalSDRTbl,SIXTY,   TWENTY  ,.160 ,.143 ,.130 ,.121 ,.116 ,.113);
        add(singleMaternalSinglePaternalSDRTbl,SEVENTY, TWENTY  ,.199 ,.179 ,.164 ,.155 ,.149 ,.145);    
                         
        add(singleMaternalSinglePaternalSDRTbl,TWENTY,  THIRTY  ,.005 ,.004 ,.003 ,.003 ,.003);
        add(singleMaternalSinglePaternalSDRTbl,THIRTY,  THIRTY  ,.016 ,.014 ,.012 ,.011 ,.010);
        add(singleMaternalSinglePaternalSDRTbl,FOURTY,  THIRTY   ,.042 ,.036 ,.032 ,.030 ,.029);
        add(singleMaternalSinglePaternalSDRTbl,FIFTY,   THIRTY  ,.081 ,.071 ,.065 ,.061 ,.058);
        add(singleMaternalSinglePaternalSDRTbl,SIXTY,   THIRTY  ,.124 ,.111 ,.102 ,.097 ,.094);
        add(singleMaternalSinglePaternalSDRTbl,SEVENTY, THIRTY  ,.179 ,.144 ,.134 ,.128 ,.124);  
                              
        add(singleMaternalSinglePaternalSDRTbl,TWENTY,  FOURTY  ,.003 ,.003 ,.002 ,.002);
        add(singleMaternalSinglePaternalSDRTbl,THIRTY,  FOURTY  ,.011 ,.010 ,.009 ,.008);
        add(singleMaternalSinglePaternalSDRTbl,FOURTY,  FOURTY  ,.030 ,.026 ,.024 ,.023);
        add(singleMaternalSinglePaternalSDRTbl,FIFTY,   FOURTY  ,.061 ,.055 ,.050 ,.048);
        add(singleMaternalSinglePaternalSDRTbl,SIXTY,   FOURTY  ,.098 ,.089 ,.083 ,.080);      
        add(singleMaternalSinglePaternalSDRTbl,SEVENTY, FOURTY  ,.128 ,.118 ,.112 ,.108);  
                 
        add(singleMaternalSinglePaternalSDRTbl,TWENTY,  FIFTY   ,.002 ,.002 ,.002);
        add(singleMaternalSinglePaternalSDRTbl,THIRTY,  FIFTY   ,.008 ,.007 ,.006);
        add(singleMaternalSinglePaternalSDRTbl,FOURTY,  FIFTY   ,.022 ,.020 ,.019);
        add(singleMaternalSinglePaternalSDRTbl,FIFTY,   FIFTY   ,.048 ,.044 ,.041);
        add(singleMaternalSinglePaternalSDRTbl,SIXTY,   FIFTY   ,.080 ,.074 ,.071);
        add(singleMaternalSinglePaternalSDRTbl,SEVENTY, FIFTY   ,.108 ,.102 ,.098); 
                 
        add(singleMaternalSinglePaternalSDRTbl,TWENTY,  SIXTY   ,.002 ,.001);
        add(singleMaternalSinglePaternalSDRTbl,THIRTY,  SIXTY   ,.006 ,.005);
        add(singleMaternalSinglePaternalSDRTbl,FOURTY,  SIXTY   ,.018 ,.016);
        add(singleMaternalSinglePaternalSDRTbl,FIFTY,   SIXTY   ,.039 ,.037);
        add(singleMaternalSinglePaternalSDRTbl,SIXTY,   SIXTY   ,.068 ,.065);
        add(singleMaternalSinglePaternalSDRTbl,SEVENTY, SIXTY   ,.095 ,.091);
                
        add(singleMaternalSinglePaternalSDRTbl,TWENTY,  SEVENTY  ,.001);
        add(singleMaternalSinglePaternalSDRTbl,THIRTY,  SEVENTY  ,.005);
        add(singleMaternalSinglePaternalSDRTbl,FOURTY,  SEVENTY  ,.015);
        add(singleMaternalSinglePaternalSDRTbl,FIFTY,   SEVENTY  ,.034);
        add(singleMaternalSinglePaternalSDRTbl,SIXTY,   SEVENTY  ,.062);
        add(singleMaternalSinglePaternalSDRTbl,SEVENTY, SEVENTY  ,.087);
           
        
        //init mother and maternal aunt
        add(motherAndMaternalAuntTbl,TWENTY,  TWENTY ,.019 ,.018 ,.017  ,.016 ,.014 ,.012);
	add(motherAndMaternalAuntTbl,THIRTY,  TWENTY ,.064 ,.062 ,.058  ,.054 ,.047 ,.040);
	add(motherAndMaternalAuntTbl,FOURTY,  TWENTY ,.153 ,.148 ,.141  ,.129 ,.115 ,.098);
	add(motherAndMaternalAuntTbl,FIFTY,   TWENTY ,.273 ,.265 ,.251  ,.232 ,.206 ,.178);
	add(motherAndMaternalAuntTbl,SIXTY,   TWENTY ,.382 ,.371 ,.353  ,.327 ,.293 ,.254);
	add(motherAndMaternalAuntTbl,SEVENTY, TWENTY ,.450 ,.437 ,.417  ,.388 ,.349 ,.305);   
	
	add(motherAndMaternalAuntTbl,TWENTY,  THIRTY ,.018 ,.017 ,.016 ,.014 ,.011,.009 );
	add(motherAndMaternalAuntTbl,THIRTY,  THIRTY ,.061 ,.058 ,.053 ,.046 ,.039,.031 );
	add(motherAndMaternalAuntTbl,FOURTY,  THIRTY ,.147 ,.139 ,.128 ,.112 ,.094,.076 );
	add(motherAndMaternalAuntTbl,FIFTY,   THIRTY ,.262 ,.249 ,.229 ,.203 ,.172,.140 );
	add(motherAndMaternalAuntTbl,SIXTY,   THIRTY ,.367 ,.350 ,.323 ,.287 ,.246,.204 );
	add(motherAndMaternalAuntTbl,SEVENTY, THIRTY ,.433 ,.414 ,.383 ,.343 ,.296,.248 );    
	                                                          
	add(motherAndMaternalAuntTbl,TWENTY,  FOURTY ,.017 ,.015 ,.013 ,.011 ,.009 ,.006);
	add(motherAndMaternalAuntTbl,THIRTY,  FOURTY ,.057 ,.052 ,.046 ,.038 ,.030 ,.022);
	add(motherAndMaternalAuntTbl,FOURTY,  FOURTY ,.137 ,.125 ,.110 ,.092 ,.073 ,.056);
	add(motherAndMaternalAuntTbl,FIFTY,   FOURTY ,.245 ,.225 ,.199 ,.167 ,.134 ,.106);
	add(motherAndMaternalAuntTbl,SIXTY,   FOURTY ,.344 ,.317 ,.282 ,.240 ,.196 ,.158);           
	add(motherAndMaternalAuntTbl,SEVENTY, FOURTY ,.407 ,.377 ,.338 ,.289 ,.239 ,.196);  
	         
	add(motherAndMaternalAuntTbl,TWENTY,  FIFTY   ,.015 ,.013 ,.011 ,.008 ,.006,.004);
	add(motherAndMaternalAuntTbl,THIRTY,  FIFTY   ,.051 ,.044 ,.036 ,.028 ,.021,.016);
	add(motherAndMaternalAuntTbl,FOURTY,  FIFTY   ,.122 ,.107 ,.089 ,.070 ,.053,.040);
	add(motherAndMaternalAuntTbl,FIFTY,   FIFTY   ,.220 ,.194 ,.162 ,.130 ,.101,.078);
	add(motherAndMaternalAuntTbl,SIXTY,   FIFTY   ,.311 ,.275 ,.233 ,.190 ,.151,.121);
	add(motherAndMaternalAuntTbl,SEVENTY, FIFTY   ,.369 ,.329 ,.281 ,.233 ,.188,.154); 
	         
	add(motherAndMaternalAuntTbl,TWENTY,  SIXTY   ,.013 ,.010 ,.008 ,.006 ,.004 ,.003);
	add(motherAndMaternalAuntTbl,THIRTY,  SIXTY   ,.043 ,.035 ,.027 ,.020 ,.015 ,.011);
	add(motherAndMaternalAuntTbl,FOURTY,  SIXTY   ,.104 ,.086 ,.067 ,.051 ,.038 ,.029);
	add(motherAndMaternalAuntTbl,FIFTY,   SIXTY   ,.187 ,.157 ,.125 ,.096 ,.075 ,.059);
	add(motherAndMaternalAuntTbl,SIXTY,   SIXTY   ,.267 ,.226 ,.183 ,.145 ,.116 ,.094);
	add(motherAndMaternalAuntTbl,SEVENTY, SIXTY   ,.320 ,.274 ,.225 ,.182 ,.148 ,.124);
	         
	add(motherAndMaternalAuntTbl,TWENTY,  SEVENTY  ,.010 ,.008 ,.006 ,.004 ,.003 ,.002);
	add(motherAndMaternalAuntTbl,THIRTY,  SEVENTY  ,.034 ,.026 ,.019 ,.014 ,.010 ,.008);
	add(motherAndMaternalAuntTbl,FOURTY,  SEVENTY  ,.082 ,.065 ,.049 ,.036 ,.027 ,.021);
	add(motherAndMaternalAuntTbl,FIFTY,   SEVENTY  ,.151 ,.121 ,.093 ,.071 ,.056 ,.046);
	add(motherAndMaternalAuntTbl,SIXTY,   SEVENTY  ,.218 ,.178 ,.141 ,.111 ,.090 ,.077);
	add(motherAndMaternalAuntTbl,SEVENTY, SEVENTY  ,.264 ,.219 ,.177 ,.143 ,.120 ,.105); 
        
        //init mother and paternal aunt
        add(motherAndPaternalAuntTbl,TWENTY,  TWENTY ,.010 ,.009 ,.008 ,.008 ,.007 ,.007);
	add(motherAndPaternalAuntTbl,THIRTY,  TWENTY ,.033 ,.030 ,.028 ,.026 ,.025 ,.025);
	add(motherAndPaternalAuntTbl,FOURTY,  TWENTY ,.080 ,.073 ,.068 ,.065 ,.063 ,.062);
	add(motherAndPaternalAuntTbl,FIFTY,   TWENTY ,.148 ,.136 ,.127 ,.121 ,.117 ,.115);
	add(motherAndPaternalAuntTbl,SIXTY,   TWENTY ,.214 ,.198 ,.186 ,.178 ,.173 ,.170);
	add(motherAndPaternalAuntTbl,SEVENTY, TWENTY ,.260 ,.241 ,.228 ,.219 ,.214 ,.210);                                    

	add(motherAndPaternalAuntTbl,TWENTY,  THIRTY  ,.008 ,.007 ,.006 ,.005 ,.005 ,.005);
	add(motherAndPaternalAuntTbl,THIRTY,  THIRTY  ,.026 ,.023 ,.021 ,.019 ,.018 ,.018);
	add(motherAndPaternalAuntTbl,FOURTY,  THIRTY  ,.064 ,.057 ,.052 ,.048 ,.046 ,.045);
	add(motherAndPaternalAuntTbl,FIFTY,   THIRTY  ,.119 ,.108 ,.097 ,.092 ,.089 ,.086);
	add(motherAndPaternalAuntTbl,SIXTY,   THIRTY  ,.176 ,.160 ,.148 ,.140 ,.134 ,.131);
	add(motherAndPaternalAuntTbl,SEVENTY, THIRTY  ,.257 ,.185 ,.180 ,.176 ,.170 ,.166);  
	                                                          
	add(motherAndPaternalAuntTbl,TWENTY,  FOURTY ,.006 ,.005 ,.004 ,.004 ,.004 ,.003);
	add(motherAndPaternalAuntTbl,THIRTY,  FOURTY ,.021 ,.018 ,.016 ,.014 ,.013 ,.012);
	add(motherAndPaternalAuntTbl,FOURTY,  FOURTY ,.053 ,.045 ,.040 ,.036 ,.034 ,.032);
	add(motherAndPaternalAuntTbl,FIFTY,   FOURTY ,.100 ,.087 ,.078 ,.071 ,.068 ,.065);
	add(motherAndPaternalAuntTbl,SIXTY,   FOURTY ,.150 ,.132 ,.120 ,.111 ,.106 ,.102);               
	add(motherAndPaternalAuntTbl,SEVENTY, FOURTY ,.216 ,.197 ,.154 ,.144 ,.138 ,.134);  
	         
	add(motherAndPaternalAuntTbl,TWENTY,  FIFTY   ,.005 ,.004 ,.003 ,.003 ,.002 ,.002 );
	add(motherAndPaternalAuntTbl,THIRTY,  FIFTY   ,.018 ,.014 ,.012 ,.010 ,.009 ,.009 );
	add(motherAndPaternalAuntTbl,FOURTY,  FIFTY   ,.045 ,.037 ,.032 ,.028 ,.026 ,.024 );
	add(motherAndPaternalAuntTbl,FIFTY,   FIFTY   ,.087 ,.074 ,.064 ,.058 ,.054 ,.051 );
	add(motherAndPaternalAuntTbl,SIXTY,   FIFTY   ,.132 ,.114 ,.101 ,.093 ,.087 ,.084 );
	add(motherAndPaternalAuntTbl,SEVENTY, FIFTY   ,.187 ,.167 ,.152 ,.123 ,.116 ,.113 ); 
	         
	add(motherAndPaternalAuntTbl,TWENTY,  SIXTY   ,.004 ,.003 ,.003 ,.002 ,.002 ,.002);
	add(motherAndPaternalAuntTbl,THIRTY,  SIXTY   ,.016 ,.012 ,.010 ,.008 ,.007 ,.007);
	add(motherAndPaternalAuntTbl,FOURTY,  SIXTY   ,.041 ,.033 ,.027 ,.023 ,.021 ,.019);
	add(motherAndPaternalAuntTbl,FIFTY,   SIXTY   ,.079 ,.065 ,.055 ,.049 ,.042 ,.042);
	add(motherAndPaternalAuntTbl,SIXTY,   SIXTY   ,.121 ,.103 ,.090 ,.081 ,.076 ,.072);
	add(motherAndPaternalAuntTbl,SEVENTY, SIXTY   ,.168 ,.147 ,.132 ,.122 ,.103 ,.100);
	         
	add(motherAndPaternalAuntTbl,TWENTY,  SEVENTY  ,.004 ,.003 ,.002 ,.002 ,.001 ,.001);
	add(motherAndPaternalAuntTbl,THIRTY,  SEVENTY  ,.015 ,.011 ,.009 ,.007 ,.006 ,.005);
	add(motherAndPaternalAuntTbl,FOURTY,  SEVENTY  ,.038 ,.030 ,.024 ,.020 ,.018 ,.016);
	add(motherAndPaternalAuntTbl,FIFTY,   SEVENTY  ,.074 ,.060 ,.050 ,.044 ,.039 ,.037);
	add(motherAndPaternalAuntTbl,SIXTY,   SEVENTY  ,.116 ,.097 ,.083 ,.074 ,.068 ,.065);
	add(motherAndPaternalAuntTbl,SEVENTY, SEVENTY  ,.148 ,.127 ,.112 ,.101 ,.095 ,.092);
        
        //init two sdr either both paternal or maternal
        add(twoSDRTbl,TWENTY,  TWENTY  ,.010 ,.009 ,.009 ,.008 ,.007 ,.006);
        add(twoSDRTbl,THIRTY,  TWENTY  ,.033 ,.032 ,.030 ,.028 ,.025 ,.021 );
        add(twoSDRTbl,FOURTY,  TWENTY  ,.081 ,.079 ,.075 ,.069 ,.062 ,.054 );
        add(twoSDRTbl,FIFTY,   TWENTY  ,.149 ,.145 ,.138 ,.129 ,.116 ,.102 );
        add(twoSDRTbl,SIXTY,   TWENTY  ,.216 ,.210 ,.201 ,.188 ,.171 ,.152 );
        add(twoSDRTbl,SEVENTY, TWENTY  ,.262 ,.256 ,.245 ,.231 ,.211 ,.189 );    
                         
        add(twoSDRTbl,TWENTY,  THIRTY  ,.009 ,.008 ,.007 ,.006 ,.005);
        add(twoSDRTbl,THIRTY,  THIRTY  ,.030 ,.028 ,.025 ,.021 ,.017);
        add(twoSDRTbl,FOURTY,  THIRTY  ,.075 ,.069 ,.061 ,.052 ,.043);
        add(twoSDRTbl,FIFTY,   THIRTY  ,.138 ,.128 ,.115 ,.099 ,.084);
        add(twoSDRTbl,SIXTY,   THIRTY  ,.201 ,.188 ,.170 ,.149 ,.128);
        add(twoSDRTbl,SEVENTY, THIRTY  ,.245 ,.230 ,.200 ,.186 ,.162);  
                              
        add(twoSDRTbl,TWENTY,  FOURTY  ,.007 ,.006 ,.005 ,.004);
        add(twoSDRTbl,THIRTY,  FOURTY  ,.024 ,.020 ,.016 ,.013);
        add(twoSDRTbl,FOURTY,  FOURTY  ,.061 ,.052 ,.042 ,.034);
        add(twoSDRTbl,FIFTY,   FOURTY  ,.114 ,.098 ,.082 ,.067);
        add(twoSDRTbl,SIXTY,   FOURTY  ,.169 ,.147 ,.124 ,.105);      
        add(twoSDRTbl,SEVENTY, FOURTY  ,.209 ,.184 ,.159 ,.137);  
                 
        add(twoSDRTbl,TWENTY,  FIFTY   ,.005 ,.003 ,.003);
        add(twoSDRTbl,THIRTY,  FIFTY   ,.016 ,.012 ,.010);
        add(twoSDRTbl,FOURTY,  FIFTY   ,.052 ,.042 ,.033);
        add(twoSDRTbl,FIFTY,   FIFTY   ,.081 ,.066 ,.054);
        add(twoSDRTbl,SIXTY,   FIFTY   ,.124 ,.104 ,.088);
        add(twoSDRTbl,SEVENTY, FIFTY   ,.158 ,.135 ,.117); 
                                                         
        add(twoSDRTbl,TWENTY,  SIXTY   ,.002 ,.002);
        add(twoSDRTbl,THIRTY,  SIXTY   ,.009 ,.007);
        add(twoSDRTbl,FOURTY,  SIXTY   ,.026 ,.020);
        add(twoSDRTbl,FIFTY,   SIXTY   ,.053 ,.044);
        add(twoSDRTbl,SIXTY,   SIXTY   ,.087 ,.075);
        add(twoSDRTbl,SEVENTY, SIXTY   ,.116 ,.103);
                
        add(twoSDRTbl,TWENTY,  SEVENTY  ,.001);
        add(twoSDRTbl,THIRTY,  SEVENTY  ,.006);
        add(twoSDRTbl,FOURTY,  SEVENTY  ,.017);
        add(twoSDRTbl,FIFTY,   SEVENTY  ,.038);
        add(twoSDRTbl,SIXTY,   SEVENTY  ,.067);
        add(twoSDRTbl,SEVENTY, SEVENTY  ,.094);
        
    }
    
    public static void add(Map<ClausDelegate.AgeKey,Map<ClausDelegate.AgeKey,Double>> table, ClausDelegate.AgeKey probandAgeKey,double twentiesVal, double thirtiesVal,double fourtiesVal, double fiftiesVal, double sixtiesVal, double seventiesVal) {
        Map<ClausDelegate.AgeKey,Double> map = new HashMap();
        map.put(TWENTY,twentiesVal);
        map.put(THIRTY,thirtiesVal);
        map.put(FOURTY,fourtiesVal);
        map.put(FIFTY,fiftiesVal);
        map.put(SIXTY,sixtiesVal);
        map.put(SEVENTY,seventiesVal);
        table.put(probandAgeKey, map);
    }
    
    /*   EIGHTY(80,89) supporting 80 would mean changing the builder logic to handle variable
     * lifetimes between scenarios.  Have decided we won't do this.  Consequence is we
     * will currently just truncate the proviced data for the ovarian scenarios
    
    */
    
     private static void add(Map<ClausDelegate.AgeKey,Map<ClausDelegate.AgeKey,Double>> table, ClausDelegate.AgeKey probandAgeKey,double twentiesVal, double thirtiesVal,double fourtiesVal, double fiftiesVal, double sixtiesVal, double seventiesVal, double eightiesValue) {
        Map<ClausDelegate.AgeKey,Double> map = new HashMap();
        map.put(TWENTY,twentiesVal);
        map.put(THIRTY,thirtiesVal);
        map.put(FOURTY,fourtiesVal);
        map.put(FIFTY,fiftiesVal);
        map.put(SIXTY,sixtiesVal);
        map.put(SEVENTY,seventiesVal);
       // map.put(EIGHTY,eightiesValue);  truncate this as we will not support it. if we do support it in the
        //we will need to take into account that the tables between breast and ovarian have a diffrent interface.
        table.put(probandAgeKey, map);
    }
    
    public static void add(Map<ClausDelegate.AgeKey,Map<ClausDelegate.AgeKey,Map<ClausDelegate.AgeKey,Double>>> table, ClausDelegate.AgeKey probandAgeKey, ClausDelegate.AgeKey youngestRelativeAgeKey,Double...values) {
        Iterator<Double> tableVals = Lists.reverse(Arrays.asList(values)).iterator();
        List allAges = Arrays.asList(ClausDelegate.AgeKey.values()).subList(0,(ClausDelegate.AgeKey.values().length -1)); //remove Unsupported
        Iterator <ClausDelegate.AgeKey> ages =  Lists.reverse(allAges).iterator();
        Map<ClausDelegate.AgeKey,Double> map = new HashMap();
        while (tableVals.hasNext()) {
            map.put(ages.next(),tableVals.next());
        }
        Map<ClausDelegate.AgeKey, Map<ClausDelegate.AgeKey, Double>> firstRelativeMap = table.get(probandAgeKey);
        if (firstRelativeMap == null) {
            firstRelativeMap = new HashMap();
            table.put(probandAgeKey, firstRelativeMap);
        }
        firstRelativeMap.put(youngestRelativeAgeKey, map);
    }
    
}
