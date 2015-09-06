package com.bosch.sbt.parent

import sbt._
import sbt.Keys._
import plugins.JvmPlugin

object BoschParent extends AutoPlugin {

  // anything you put in here will be available to your projects build.sbt
  object autoImport {

      // spring libraries
      val springContext = "org.springframework" % "spring-context" % "4.1.6.RELEASE" ;
      val springCore = "org.springframework" % "spring-core" % "4.1.6.RELEASE" ;
      val springExpression = "org.springframework" % "spring-expression" % "4.1.6.RELEASE" ;
      val springBean = "org.springframework" % "spring-beans" % "4.1.6.RELEASE" ;
      val springAop = "org.springframework" % "spring-aop" % "4.1.6.RELEASE" ;
      val springContextSupport = "org.springframework" % "spring-context-support" % "4.1.6.RELEASE" ;
      val springTX = "org.springframework" % "spring-tx" % "4.1.6.RELEASE" ;
      val springJdbc = "org.springframework" % "spring-jdbc" % "4.1.6.RELEASE" ;
      val springORM = "org.springframework" % "spring-orm" % "4.1.6.RELEASE" ;
      val springOXM = "org.springframework" % "spring-oxm" % "4.1.6.RELEASE" ; 
      val springWeb = "org.springframework" % "spring-web" % "4.1.6.RELEASE" ;
      val springWebMVC = "org.springframework" % "spring-webmvc" % "4.1.6.RELEASE" ;
      val springWebMVCPortlet = "org.springframework" % "spring-webmvc-portlet" % "4.1.6.RELEASE" ;
      val springTest = "org.springframework" % "spring-test" % "4.1.6.RELEASE" ;
      val springBatchCore = "org.springframework" % "spring-batch-core" % "3.0.3.RELEASE" ;
      val springBOM = "org.springframework" % "spring-framework-bom" % "4.1.6.RELEASE" ;
      val springSecurity = "org.springframework.security" % "spring-security-core" % "4.0.1.RELEASE"

      // hibernate libraries
      val hibernateCore = "org.hibernate" % "hibernate-core" % "4.3.8.Final" ;
      val hibernateValidator = "org.hibernate" % "hibernate-validator" % "5.1.3.Final" ;
      val hibernateEntityManager = "org.hibernate" % "hibernate-entitymanager" % "4.3.9.Final" ;
      val hibernatePersistance = "org.hibernate.javax.persistence" % "hibernate-jpa-2.0-api" % "1.0.1.Final" ;
      val hibernateAnnotations = "org.hibernate" % "hibernate-annotations" % "3.5.6-Final" ;
      val hibernateCommonsAnnotations = "org.hibernate" % "hibernate-commons-annotations" % "3.2.0.Final" ;
      val expressionLanguage = "javax.el" % "javax.el-api" % "2.2.4"

      // play plugins
      val deadboltCore = "be.objectify" % "deadbolt-core_2.11" % "2.3.3" ;
      val deadboltJava = "be.objectify" % "deadbolt-java_2.11" % "2.3.3" ;

      // opensource libraries
      val gson = "com.google.code.gson" % "gson" % "2.3.1" ;
      val hikariCP = "com.zaxxer" % "HikariCP" % "2.3.7" ;
      val bouncycastle = "org.bouncycastle" % "bcprov-jdk16" % "1.46" ;
      val jasypt = "org.jasypt" % "jasypt" % "1.9.2" ;
      val jasyptSpring = "org.jasypt" % "jasypt-spring3" % "1.9.2" ;
      val slf4j = "org.slf4j" % "slf4j-api" % "1.7.12" ;
      val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.1.3" ;
      val logbackCore = "ch.qos.logback" % "logback-core" % "1.1.3" ;
      val junit = "junit" % "junit" % "4.12" % "test" intransitive() ;
      val easymock = "org.easymock" % "easymock" % "3.3.1" ;
      val quartz = "org.quartz-scheduler" % "quartz" % "2.2.1" ;
      val jacksonCore = "com.fasterxml.jackson.core" % "jackson-core" % "2.5.3" ;
      val jacksonAnnotations = "com.fasterxml.jackson.core" % "jackson-annotations" % "2.5.3" ;
      val jacksonDatabind = "com.fasterxml.jackson.core" % "jackson-databind" % "2.5.3" ;
    
      // apache libraries
      val log4j = "log4j" % "log4j" % "1.2.17" ;
      val cglib = "cglib" % "cglib" % "3.1" ;
      val commonsCollection = "commons-collections" % "commons-collections" % "3.2.1";
      val commonsBeanUtils = "commons-beanutils" % "commons-beanutils" % "1.9.2";
      val commonsLang3 = "org.apache.commons" % "commons-lang3" % "3.4" ;
      val commonsLang = "commons-lang" % "commons-lang" % "2.6" ;
      val commonsCodec = "commons-codec" % "commons-codec" % "1.10" ;
      val commonsIO = "commons-io" % "commons-io" % "2.4" ;
      val commonsConfiguration = "commons-configuration" % "commons-configuration" % "1.10" ;

      // others
      val asm = "asm" % "asm" % "3.3.1" ;
      val aopAlliance = "aopalliance" % "aopalliance" % "1.0" ;
      val antlr = "antlr" % "antlr" % "2.7.7" ;
      val javassist = "org.javassist" % "javassist" % "3.19.0-GA" ;
      val dom4J = "dom4j" % "dom4j" % "1.6.1" ;
      val jsonLib = "org.json" % "json" % "20141113";
      val objenesis = "org.objenesis" % "objenesis" % "2.1" ;
  }

  // This ensures that the plugin gets automatically enabled as long as it's
  // added in the plugins.sbt file.
  override def trigger = allRequirements
  override def requires = JvmPlugin

  override def projectSettings: Seq[Setting[_]] = Seq(
    // Any settings you add here will be automatically applied to all your projects
    // for example, you might want to enforce a set of Java compiler options to every project:
    javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:-options")
  )
}