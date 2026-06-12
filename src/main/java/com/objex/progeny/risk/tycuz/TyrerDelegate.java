/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.objex.progeny.risk.tycuz;

import com.objex.progeny.risk.Utils;
import com.objex.risk.beans.AffectedType;
import static com.objex.risk.beans.Germline.*;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.RiskError;
import com.objex.risk.beans.TestResult;
import com.objex.risk.beans.tcuzick.Person;
import com.objex.risk.beans.tcuzick.Person.DensityMeasure;
import com.objex.risk.beans.tcuzick.Person.Ethnic;
import com.objex.risk.beans.tcuzick.Person.HRTUse;
import com.objex.risk.beans.tcuzick.Person.HyperplasiaType;
import com.objex.risk.beans.tcuzick.Risk;
import com.objex.risk.beans.tcuzick.TyrerResult;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author jason
 */
@Service
public class TyrerDelegate {

    final static Logger LOG = LoggerFactory.getLogger(TyrerDelegate.class);
    private static String OS = System.getProperty("os.name").toLowerCase();
    public static final String TYRER_VERSION = "Tyrer-Cuzick Version 8.0b";
    private String command = null;
    private String inputSwitch = "-i ";
    private String outputSwitch = "-o ";
    private String mortalitySwitch = "-m 1 ";
    private File workPath = null;
    public static final String DELIM = "\t";
    protected static AtomicInteger count = new AtomicInteger(0);
    protected static HashMap<DensityMeasure, String> denstityRangeVal = new HashMap<DensityMeasure, String>() {{
        put(DensityMeasure.VOLPRA,"Volpara Percentage must be between 0 and 100");
        put(DensityMeasure.VAS,"VAS Percentage must be between 0 and 100");
   }};

    public TyrerDelegate(String execCommand) {
        this.command = execCommand;
        if (!OS.startsWith("win")) {
            inputSwitch = "";
            outputSwitch = "";
            mortalitySwitch = "-m ";
        }
        workPath = new File(command.substring(0,this.command.lastIndexOf(File.separator)) + "/work");
        LOG.info("Using command :" + command + "  Using path: " + workPath.getAbsolutePath());
        if (!workPath.exists()) {
            workPath.mkdir();
        }
    }

    public static void main(String[] args) throws Exception {

        String path = "C:\\temp\\tyrer\\tcuzick-wincmd.exe";
        TyrerDelegate del = new TyrerDelegate(path);
        Risk risk = new Risk();
        Person proband = new Person();
        proband.setAge(36);
        proband.setMenarcheAge(12);
        proband.setHeight(1.5);
        proband.setWeight(240.0);
        proband.setId(1);
        proband.setMotherId(2);
        proband.setFatherId(3);
       // proband.setHasLCIS(true);
        proband.setAffectedOvarianType(AffectedType.ONE);
        proband.setDiagnoseOvarianAge(30);
        proband.setLiveBirthAge(17);
        proband.setHasLiveBirth(true);
       // proband.setParous(Person.ParousType.Parous);
        proband.setMenostat(Person.MenosType.preMenopausal);
        proband.setEthnic(Ethnic.AJ);
        risk.add(proband);

        Person mother = new Person();
        mother.setId(2);
        mother.setMotherId(7);
        mother.setAffectedBreastType(1);
        mother.setDiagnoseBreastAge(35);
        risk.add(mother);


        Person dad = new Person();
        dad.setId(3);
        dad.setGender(1);
        dad.setMotherId(8);
        risk.add(dad);

        Person sister1 = new Person();
        sister1.setId(4);
        sister1.setMotherId(2);
        sister1.setFatherId(3);
        sister1.setAffectedBreastType(AffectedType.ONE);
        sister1.setDiagnoseBreastAge(33);
        risk.add(sister1);

        Person sister2 = new Person();
        sister2.setId(5);
        sister2.setMotherId(2);
        sister2.setFatherId(3);
        sister2.setAffectedOvarianType(AffectedType.ONE);
        sister2.setDiagnoseOvarianAge(29);
        sister2.setAge(34);
        risk.add(sister2);

        Person daughter = new Person();
        daughter.setId(6);
        daughter.setMotherId(1);
        daughter.setAge(19);
        risk.add(daughter);

        Person matGrand = new Person();
        matGrand.setId(7);
        matGrand.setAge(73);
        risk.add(matGrand);

        Person patGrand = new Person();
        patGrand.setId(8);
        patGrand.setAge(76);
        risk.add(patGrand);

        //Utils.saveToFile("C:\\temp\\tyrer\\saveOffTyrer.xml", risk);
        del.calculate(risk);
    }

    private void add(StringBuilder file, String value) {
        file.append(value).append(DELIM);
    }

    private void add(StringBuilder file, Number value) {
        file.append(value).append(DELIM);
    }

    private TyrerResult buildResult(String[] split, int age, int ageTo, int ageBy) {
       TyrerResult result = new TyrerResult();
       result.setVersion(TYRER_VERSION);

       /*
       Double five  = parseResult(split[5]);
       Double life  = parseResult(split[21]);
       Double brca1  = parseResult(split[23]);
       Double brca2  = parseResult(split[24]);
       System.out.println("splitted 5 " + five + " splitted 21 " + life);
       System.out.println("splitted23 BRCA1 " + split[23] + " splitted 24 BRCA2 " + split[24]);
       result.setProbabilityCarrier(brca1 + brca2);
       result.setProbabilityMutationBRCA1(brca1);
       result.setProbabilityMutationBRCA2(brca2);
       Prediction pred = result.addPrediction();
       pred.addRisk("breastRisk", five);
       pred.setAge(age + 5);
       pred = result.addPrediction();
       pred.addRisk("breastRisk", life);
       pred.setAge(85);
       */
       Double brca1  = parseResult(split[23]);
       Double brca2  = parseResult(split[24]);
       result.setProbabilityCarrier(brca1 + brca2);
       result.setProbabilityMutationBRCA1(brca1);
       result.setProbabilityMutationBRCA2(brca2);

       int predictionAge = age + ageBy;
       int predictionIndex = ageBy;
        //Start with 5 year risk, and loop to honor ageBy
        while (predictionAge < ageTo && predictionIndex < 21) {
            Double absoluteRisk = parseResult(split[predictionIndex]);
            Double populationRisk = parseResult(split[predictionIndex + 24]);
            Prediction pred = result.addPrediction();
            pred.addRisk("breastRisk", absoluteRisk);
            pred.addRisk("populationRisk", populationRisk);
            pred.setAge(predictionAge);
            predictionAge = predictionAge + ageBy;
            predictionIndex = predictionIndex + ageBy;
        }
        // Calculate lifetime risk. this ensures it honors ageTo for lifetime even if it isn't evenly divisible
        Double life  = parseResult(split[21]);
        Double lifePopulationRisk  = parseResult(split[45]);
        Prediction pred = result.addPrediction();
        pred.addRisk("breastRisk", life);
        pred.addRisk("populationRisk", lifePopulationRisk);
        pred.setAge(85);
        return result;
}

/**
 * Method catches Number Exceptions that model runtime returns for some failures.  Returning
 * null in this eventuality allows Progeny to interpret the result as Not Available(NA) rather
 * than throwing an Exception.
 * @param value
 * @return
 */
private Double parseResult(String value) {
    try {
        return new Double(value) / 100;
    } catch (NumberFormatException e) {
        LOG.warn("tyrer program result not numeric " + value);
        return null;
    }
}
public TyrerResult calculate(Risk risk) throws IOException, InterruptedException {

    TyrerResult result = new TyrerResult();
    validateData(risk.getFamily().get(0), result, risk);
    if (result.hasErrors()) {
        return result;
    }
    String stamp = stamp() + ".txt";
    String inputFile = workPath.getAbsolutePath() + "/tyInput" + stamp;
    String outputFile = workPath.getAbsolutePath() + "/tyOutput" + stamp;


    StringBuilder file = new StringBuilder("v8\r\n1\r\n");
    Person proband = risk.getFamily().get(0);

    add(file, proband.getId()); //1
    add(file, proband.getAge());   //2
    add(file, proband.getMenarcheAge());//3
    add(file, proband.getParous().value); //4
    if (proband.getHasLiveBirth()) { //5
    //if (proband.getHasLiveBirth() != null && proband.getHasLiveBirth()) { //5
       add(file, proband.getLiveBirthAge());
    } else {
       add(file, -99);
    }
    add(file,proband.getMenostat().value); //6
    add(file,proband.getMenopauseAge()); //7
    add(file,proband.getHeight());//8
    add(file,proband.getWeight()); //9
    add(file,proband.getHyperplasia().value); //10
    addBoolInt(proband.getHasAtypicalHyperplasia(), file); //11
    addBoolInt(proband.getHasLCIS(), file); //12
    add(file,0);  //13???????????????
    addBoolInt(proband.getAffectedOvarianType() != AffectedType.NONE, file); //14
    add(file,proband.getDiagnoseOvarianAge());   //15
    addBoolInt(proband.getEthnic() == Ethnic.AJ, file);   //16
    HRTUse use = proband.getUsedHRT();
    add(file,use.value);   //17
    if (use == HRTUse.NEVER ||  use == HRTUse.PRIOR_OUTSIDE_FIVE || proband.getHrtType() == null) {  //18
         add(file,1);
    } else {
        add(file,proband.getHrtType().value);
    }
    add(file,proband.getYearsPastHRT());   //19
    add(file,proband.getYearsFutureHRT()); //20 GUI sticks 2 in here even when not used as default
    add(file,proband.getYearsSinceHRT()); //21
    Person mother = null;
    Person father = null;

    for (Person parent : Utils.getParents(proband, risk)) {
        if(parent.isMother(proband)) {
            mother = parent;
        } else if (parent.isFather(proband)) {
            father = parent;
        }
    }

    processGermline(proband, file); //22
    processGermline(father, file); //23

    processFemale(mother, file); //24-30
    List<Person> sisters = processSisters(proband, risk, file,9);
    List<Person> brothers = Utils.filterSex(Utils.getSiblings(proband, risk),false);

    Person paternalGrandma = getGrandmaWithParent(father, risk);
    processFemale(paternalGrandma,file);
    Person maternalGrandma = getGrandmaWithParent(mother, risk);
    processFemale(maternalGrandma,file);

    //paternal aunts
    List<Person> paternalAunts = processSisters(father, risk, file,9);
    //maternal aunts
    List<Person> maternalAunts = processSisters(mother, risk, file,9);

    processDaughters(proband, risk, file,9);

    int neicePositions = sisters.size();
    List<Person> brotherDaughters = Utils.filterSex(Utils.getChildren(brothers, risk),true);
    for (Person neice : brotherDaughters) {
        if (neice.getAffectedBreastType() != AffectedType.NONE) //TODO include bilateral here?
        {
            neicePositions++;
            break;
        }
    }
   add(file,neicePositions);
   for (Person p : sisters) {
       List<Person> affecteds = new ArrayList();
       List<Person> daughters = Utils.filterSex(Utils.getChildren(p, risk),true);
       for (Person daughter : daughters) {
           if (daughter.getAffectedBreastType() != AffectedType.NONE) {
              affecteds.add(daughter);
           }
       }
       processFemaleList(file, affecteds, false);
   }
   if (neicePositions > sisters.size()) { //need brother processing because an affected daughter exists
       List<Person> affecteds = new ArrayList();
       for (Person daughter : brotherDaughters) {
           if (daughter.getAffectedBreastType() != AffectedType.NONE) {
              affecteds.add(daughter);
           }
       }
       processFemaleList(file, affecteds, false);
   }
   List<Person> patHalfSisters = Utils.filterSex(Utils.getHalfSiblings(proband,father, risk),true);
   trim(patHalfSisters,9,true);
   /*
   while (patHalfSisters.size() > 9) {  //TODO tyrer program not handling after nine.  Remove after any updates to check again
        patHalfSisters.remove(patHalfSisters.size() - 1);
   } */
   processFemaleList(file, patHalfSisters ,false);

   List<Person> matHalfSisters = Utils.filterSex(Utils.getHalfSiblings(proband,mother, risk),true);
   trim(matHalfSisters,9,true);
   /*
   while (matHalfSisters.size() > 9) {  //TODO tyrer program not handling after nine.  Remove after any updates to check again
        matHalfSisters.remove(matHalfSisters.size() - 1);
   } */
   processFemaleList(file, matHalfSisters ,false);

   add(file,paternalAunts.size()); //paternal aunt positions
   for (Person aunt : paternalAunts) { //second degree pat aunt daughters
       List<Person> daughters = Utils.filterSex(Utils.getChildren(aunt, risk),true);
        processFemaleList(file, daughters ,false);
   }

   add(file,maternalAunts.size()); //maternal aunt positions
   for (Person aunt : maternalAunts) { //second degree mat aunt daughters
       List<Person> daughters = Utils.filterSex(Utils.getChildren(aunt, risk),true);
        processFemaleList(file, daughters ,false);
   }

   processUncleCousins(father, risk, file, (paternalAunts.size() < 9)); //uncles seem to need an open slot under the maxsize
   processUncleCousins(mother, risk, file,  (maternalAunts.size() < 9)); //uncles seem to need an open slot under the maxsize

   addBoolInt(father != null && father.getAffectedBreastType().isAffected(), file);

    int affectBroth = 0;
    for (Person brother : brothers) {
        if (brother.getAffectedBreastType().isAffected()) {
            affectBroth = 1;
            break;
        }
    }
    add(file, affectBroth);

    //version 8 stuff
    add(file,proband.getDensityMeasure().value);
    if (proband.getDensityMeasure().value == 0 || proband.getDensityMeasure().value == 1) {
        add(file,proband.getDensityValue());
    } else {
        add(file,proband.getDensityValue().intValue());
    }
    if (proband.getPolygenicSNP() == null || 0 == proband.getPolygenicSNP()) {
         add(file,0);
    } else {
         add(file,Math.log(proband.getPolygenicSNP()));
    }
    //end 8

    createInput(inputFile, file);

    Process p =   executeCommand(inputFile, outputFile, risk.isCompetingMortality());
    if (OS.startsWith("win")) {
        go(p);
    }
    p.waitFor();

      p.getOutputStream();
      BufferedReader reader = new BufferedReader(new FileReader(outputFile));
      String line = null;
      while ((line = reader.readLine()) != null) {
         // System.out.println(line);
          if (line.indexOf(",") > -1) {
              result = buildResult(line.split(","), proband.getAge(), risk.getAgeTo(),risk.getAgeBy());
               break;
          }
      }
      reader.close();
      if (!risk.isSaveIncoming()) {
             cleanFiles(inputFile,outputFile);
      }
      return result;
    }

    protected Process executeCommand(String inputFile, String outputFile, boolean compMortality) throws IOException {
        Runtime run =   Runtime.getRuntime();
        String mort = " " + (compMortality ? mortalitySwitch : "");
        String cmd = getCommand() + mort + inputSwitch  + inputFile + " " + outputSwitch + outputFile;
        LOG.info("attempting to run cmd: " + cmd);
        Process p = Runtime.getRuntime().exec(cmd);
        return p;
    }

    private void cleanFiles(final String inputFile, final String outputFile) {
        Thread cleaner = new Thread () {
            public void run() {
                LOG.debug("Removing input: " + inputFile);
                LOG.debug("Removing output: " + outputFile);
                File file = new File(inputFile);
                file.delete();
                file = new File(outputFile);
                file.delete();
            }};
        cleaner.start();
    }

    protected void createInput(String inputFile, StringBuilder file) {
        try {

            FileWriter fWriter = new FileWriter(inputFile);
            BufferedWriter bw = new BufferedWriter(fWriter);

            bw.write(file.toString());
            bw.flush();
            bw.close();
        } catch (Exception e) {
            System.out.println(
                    "Exception occured while getting resultset by the query");
            e.printStackTrace();
        }
    }

    protected void processUncleCousins(Person parent, Risk risk, StringBuilder file, boolean openSlots) {
        List<Person> uncles = Utils.filterSex(Utils.getSiblings(parent, risk),false);
        addBoolInt(!uncles.isEmpty() && openSlots, file);
        if (!uncles.isEmpty()  && openSlots) {
            processFemaleList(file, Utils.filterSex(Utils.getChildren(uncles, risk),true) ,false);
        }
    }

    protected List<Person> processSisters(Person person, Risk risk, StringBuilder file, int maxSize) {
        List<Person> sisters = Utils.filterSex(Utils.getSiblings(person, risk),true);
        trim(sisters,maxSize,true);
       /* while (sisters.size() > maxSize) {  //TODO tyrer program not handling after nine.  Remove after any updates to check again
           sisters.remove(sisters.size() - 1);
        } */
        processFemaleList(file, sisters,false);
        return sisters;
    }

    protected void trim(List<Person> people, int maxSize, boolean withAffectedBias) {
        List<Person> removeList = new ArrayList(people);
        while (people.size() > maxSize && !removeList.isEmpty()) {  //TODO tyrer program not handling after nine.  Remove after any updates to check again
           Person person = removeList.remove(removeList.size() - 1);
           if (withAffectedBias && (person.getAffectedBilateralBreastType().isAffected() || person.getAffectedBreastType().isAffected() || person.getAffectedOvarianType().isAffected())) {
               continue;
           } else {
               people.remove(person);
           }
        }
        if (withAffectedBias) { //MORE affected than slots
             trim(people,maxSize,false);
        }
    }

    protected void processDaughters(Person person, Risk risk, StringBuilder file, int maxSize) {
        List<Person> daughters = Utils.filterSex(Utils.getChildren(person, risk),true);
        trim(daughters,maxSize,true);
        /*
         while (daughters.size() > maxSize) {  //TODO tyrer program not handling after nine.  Remove after any updates to check again
           daughters.remove(daughters.size() - 1);
        } */
        processFemaleList(file, daughters,false);
    }

   /**
    * This process a list of related females in the way the alg expects
    * @param file
    * @param femaleRelatives daughters, aunts etc... some category of female relatives
    */
    protected void processFemaleList(StringBuilder file, List<Person> femaleRelatives, boolean includeEmpty) {
        add(file,femaleRelatives.size());
        if (femaleRelatives.isEmpty() && includeEmpty) {  //still need once for empty femaleRelatives says the spec
            processFemale(null,file);
        }
        for (Person sister : femaleRelatives) {
            processFemale(sister, file);
        }
    }

    protected Person getGrandmaWithParent(Person parent, Risk risk) {
        Person grandma = null;
        if (parent != null) {
            List<Person> grans =  Utils.filterSex(Utils.getParents(parent,risk), true);
            if (!grans.isEmpty()) {
                grandma = grans.get(0);
            }
        }   return grandma;
    }

    protected void processFemale(Person female, StringBuilder file) {
        if (female == null) {
            add(file,0);
            add(file,0);
            add(file,0);
            add(file,-99);
            add(file,-99);
            add(file,-99);
        } else {

            addBoolInt(female.getAffectedBreastType() != AffectedType.NONE, file);
            addBoolInt(female.getAffectedBilateralBreastType() != AffectedType.NONE, file);
            addBoolInt(female.getAffectedOvarianType() != AffectedType.NONE, file);

            add(file,female.getDiagnoseBreastAge());
            add(file,female.getDiagnoseBilateralBreastAge());
            add(file,female.getDiagnoseOvarianAge());
        }
        processGermline(female, file);
    }

    protected void processGermline(Person person, StringBuilder file) {
        TestResult positive = TestResult.POSITIVE;
        if (person != null && person.hasGermline()) { //TODO why do specs have BRCA1 and 2 as mutually exclusive
            if (person.getGermline(BRCA2) == positive) {
                add(file,3);
            } else if (person.getGermline(BRCA1) == positive) {
                add(file,2);
            } else { //has at least one negative test and no positives
                add(file,1);
            }
        } else {
            add(file,0);
        }
    }

    protected void addBoolInt(boolean condition, StringBuilder file) {
        if (condition) {
            add(file,1);
        } else {
            add(file,0);
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    protected synchronized String stamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String name = count.incrementAndGet() + "_" + format.format(new Date());
        return name;
    }

    protected void go(Process p) {
    final InputStream is = p.getInputStream();
    Thread eater = new Thread () {
        public void run() {
            try
            {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line=null;
                while ( (line = br.readLine()) != null)
                    System.out.println(line);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }};
    eater.start();
}
    /**
     * validate the incoming risk family data
     * @param proband
     * @param result
     * @param risk
     */
    protected void validateData(Person proband, TyrerResult result, Risk risk) {
            if (proband.getAffectedBreastType().isAffected() || proband.getAffectedBilateralBreastType().isAffected()) {
                 result.addError(new RiskError(proband.getId(),"Unsupported Cancer for member","unsupportedCancerFormember"));
            }
            if (proband.getAge() < 1 || proband.getAge() > 84) {
                 result.addError(new RiskError(proband.getId(),"Age must be between 1 and 84","ageMustBeBetweenOneAndEightyFour"));
            }
            if ((proband.getDensityMeasure() == DensityMeasure.VAS  || proband.getDensityMeasure() == DensityMeasure.VOLPRA) &&
                    (proband.getDensityValue() < 0 || proband.getDensityValue() >= 100)) {
                              result.addError(new RiskError(proband.getId(),denstityRangeVal.get(proband.getDensityMeasure()),"unsupporteDensity"));
            }
            if (proband.getGender() == 1) {
                result.addError(new RiskError(proband.getId(),"member Cannot be male","memberCannotBeMale"));
            }
           for (Person member : risk.getFamily()) {
               validateMemberData(member, result);
           }
    }

    /**
     *
     * @param member
     * @param result
     * @param risk
     */
    private void validateMemberData(Person member, TyrerResult result) {
        if (member.getGender() == 1 && member.getHasLiveBirth()) {
            result.addError(new RiskError(member.getId(),"member Cannot be male","liveBirthInvalidForMale"));
        }
        if (member.getGender() == 1 && member.getHyperplasia() != HyperplasiaType.NONE) {
            result.addError(new RiskError(member.getId(),"Hyperplasia invalid for male","hyperplasiaInvalidForMale"));
        }
        if (member.getGender() == 1 && member.getHasAtypicalHyperplasia()) {
            result.addError(new RiskError(member.getId(),"Atypical Hyperplasia invalid for male","atypicalHyperplasiaInvalidForMale"));
        }
        if (member.getGender() == 1 && member.getMenarcheAge() > 0) {
            result.addError(new RiskError(member.getId(),"Menarch Age invalid for male","menarcheAgeInvalidForMale"));
        }
        if (member.getGender() == 1 && member.getMenopauseAge() > 0) {
            result.addError(new RiskError(member.getId(),"Menapause invalid for male","menapauseAgeInvalidForMale"));
        }
        if ((member.getUsedHRT() == null || member.getUsedHRT() == Person.HRTUse.NEVER) && (member.getYearsPastHRT() > 0 || member.getHrtType() != null)) {
            result.addError(new RiskError(member.getId(),"HRT Type or Duration exists without use","HRTTypeOrDurationExistsWithoutUse"));
        }
        if (member.getDiagnoseBilateralBreastAge() > 0 && (member.getDiagnoseBreastAge() > member.getDiagnoseBilateralBreastAge()) ) {
             result.addError(new RiskError(member.getId(),"Bilateral Diagnosed before Breast","bilateralDiagnosedBeforeBreast"));
        }
        if (member.getLiveBirthAge() != null && member.getLiveBirthAge() > 0) {
              if (!member.getHasLiveBirth()) {
                  result.addError(new RiskError(member.getId(),"Missing Live Birth","missingLiveBirth"));
              }
        }
        if (member.getAge() < 0) {
            result.addError(new RiskError(member.getId(),"Invalid age","invalidAge"));
        }
    }
}
