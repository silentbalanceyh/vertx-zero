package io.modello.emf;

import io.horizon.annotations.ChatGPT;
import io.horizon.util.HUt;

/**
 * 该类定义来自 EMF 2.33.0 版本，主定义来自于：org.eclipse.emf.ecore.EcorePackage.Literals 中的定义
 * 定义主要分两类：
 * <pre><code>
 *     1. EDataType / 基础类型定义
 *     2. EClass / 复杂类型定义
 * </code></pre>
 *
 * @author lang : 2023-05-12
 */
@ChatGPT
public enum NameEType {
    // EDataType, JVM定义部分
    EString,            // http://www.eclipse.org/emf/2002/Ecore#//EString
    EBoolean,           // http://www.eclipse.org/emf/2002/Ecore#//EBoolean
    EByte,              // http://www.eclipse.org/emf/2002/Ecore#//EByte
    EChar,              // http://www.eclipse.org/emf/2002/Ecore#//EChar
    EShort,             // http://www.eclipse.org/emf/2002/Ecore#//EShort
    EInt,               // http://www.eclipse.org/emf/2002/Ecore#//EInt
    ELong,              // http://www.eclipse.org/emf/2002/Ecore#//ELong
    EFloat,             // http://www.eclipse.org/emf/2002/Ecore#//EFloat
    EDouble,            // http://www.eclipse.org/emf/2002/Ecore#//EDouble
    EDate,              // http://www.eclipse.org/emf/2002/Ecore#//EDate
    EJavaClass,         // http://www.eclipse.org/emf/2002/Ecore#//EJavaClass
    EJavaObject,        // http://www.eclipse.org/emf/2002/Ecore#//EJavaObject
    EBigDecimal,        // http://www.eclipse.org/emf/2002/Ecore#//EBigDecimal
    EBigInteger,        // http://www.eclipse.org/emf/2002/Ecore#//EBigInteger
    EMap,               // http://www.eclipse.org/emf/2002/Ecore#//EMap


    // EClass, 复杂类型定义
    EEnum,              // http://www.eclipse.org/emf/2002/Ecore#//EEnum
    EObject,            // http://www.eclipse.org/emf/2002/Ecore#//EObject
    EClass,             // http://www.eclipse.org/emf/2002/Ecore#//EClass
    EClassifier,        // http://www.eclipse.org/emf/2002/Ecore#//EClassifier
    EAnnotation,        // http://www.eclipse.org/emf/2002/Ecore#//EAnnotation
    EAttribute,         // http://www.eclipse.org/emf/2002/Ecore#//EAttribute
    EPackage,           // http://www.eclipse.org/emf/2002/Ecore#//EPackage
    EOperation,         // http://www.eclipse.org/emf/2002/Ecore#//EOperation
    EParameter,         // http://www.eclipse.org/emf/2002/Ecore#//EParameter
    EReference,         // http://www.eclipse.org/emf/2002/Ecore#//EReference
    ;

    public String uri() {
        // http://www.eclipse.org/emf/2002/Ecore#//{0}
        return HUt.fromMessage(VEmf.URI.ECORE, this.name());
    }
}
