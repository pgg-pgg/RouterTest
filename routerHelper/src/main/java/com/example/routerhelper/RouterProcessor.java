package com.example.routerhelper;

import com.example.annotation.AutoRouter;
import com.example.annotation.Component;
import com.example.annotation.Components;
import com.example.annotation.StaticRouter;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Messager mMessager;

    private Map<String,String> mStaticRouterMap=new HashMap<>();
    private List<String> mAutoRouterList=new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler=processingEnv.getFiler();
        mMessager=processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mStaticRouterMap.clear();
        mAutoRouterList.clear();

        try {
            Set<? extends Element> mainAppElement=roundEnv.getElementsAnnotatedWith(Components.class);
            if (!mainAppElement.isEmpty()){
                processInstaller(mainAppElement);
                return true;
            }
            processComponent(roundEnv);
        }catch (Exception e){
            mMessager.printMessage(Diagnostic.Kind.ERROR,e.getMessage());
        }
        return true;
    }

    private void processComponent(RoundEnvironment roundEnv) throws Exception {
        Set<? extends Element> compElements=roundEnv.getElementsAnnotatedWith(Component.class);
        if (compElements.isEmpty()){
            return;
        }

        Element item = compElements.iterator().next();
        String componentName=item.getAnnotation(Component.class).value();
        Set<? extends Element> routerElements=roundEnv.getElementsAnnotatedWith(StaticRouter.class);
        for (Element e:routerElements){
            if (!(e instanceof TypeElement)){
                continue;
            }
            TypeElement typeElement=(TypeElement)e;
            String pattern=typeElement.getAnnotation(StaticRouter.class).value();
            mStaticRouterMap.put(pattern,typeElement.getQualifiedName().toString());
        }

        Set<? extends Element> autoRouterElements=roundEnv.getElementsAnnotatedWith(AutoRouter.class);
        for (Element e:autoRouterElements){
            if (!(e instanceof TypeElement)){
                continue;
            }
            TypeElement typeElement=(TypeElement) e;
            mAutoRouterList.add(typeElement.getQualifiedName().toString());
        }
        writeComponentFile(componentName);
    }

    private void writeComponentFile(String componentName) throws Exception{
        String className=Config.FILE_PREFIX+componentName;
        JavaFileObject javaFileObject=mFiler.createSourceFile(className);

        PrintWriter printWriter=new PrintWriter(javaFileObject.openWriter());

        printWriter.println("package "+Config.PACKAGE_NAME+";");
        printWriter.println("import android.app.Activity;");
        printWriter.println("import android.app.Service;");
        printWriter.println("import android.content.BroadcastReceiver;");
        printWriter.println("public class "+className+" {");
        printWriter.println("public static void router() {");

        for (Map.Entry<String,String> entry:mStaticRouterMap.entrySet()){
            printWriter.println("com.example.router.Router.router(\""+entry.getKey()
            +"\","+entry.getValue()+".class);");
        }

        for (String klass:mAutoRouterList){
            printWriter.println("if (Activity.class.isAssignableFrom("+klass+".class)) {");
            printWriter.println("com.example.router.Router.router(com.example.router.rule.ActivityRule.ACTIVITY_SCHEME + \""
                    +klass+"\", " + klass + ".class);");
            printWriter.println("}");

            printWriter.println("else if (Service.class.isAssignableFrom(" + klass + ".class)) {");
            printWriter.println("com.example.router.Router.router(com.example.router.rule.ServiceRule.SERVICE_SCHEME + \""
                    +klass+"\", " + klass + ".class);");
            printWriter.println("}");

            printWriter.println("else if (BroadcastReceiver.class.isAssignableFrom(" + klass + ".class)) {");
            printWriter.println("com.example.router.Router.router(com.example.router.rule.ReceiverRule.RECEIVER_SCHEME + \""
                    +klass+"\", "+klass+".class);");
            printWriter.println("}");
        }

        printWriter.println("}");
        printWriter.println("}");
        printWriter.flush();
        printWriter.close();
    }

    private void processInstaller(Set<? extends Element> mainAppElement) throws IOException{

        TypeElement typeElement=(TypeElement)mainAppElement.iterator().next();
        JavaFileObject javaFileObject=mFiler.createSourceFile(Config.ROUTER_MANAGER,typeElement);
        PrintWriter writer=new PrintWriter(javaFileObject.openWriter());

        writer.println("package "+Config.PACKAGE_NAME+";");
        writer.println("public class "+Config.ROUTER_MANAGER+" {");
        writer.println("public static void "+Config.ROUTER_MANAGER_METHOD+"() {");

        Components componentsAnnotation=typeElement.getAnnotation(Components.class);
        String[] components=componentsAnnotation.value();
        for (String item:components){
            writer.println(Config.FILE_PREFIX+item+".router();");
        }

        writer.println("}");
        writer.println("}");
        writer.flush();
        writer.close();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set=new HashSet<>();
        set.add(AutoRouter.class.getCanonicalName());
        set.add(StaticRouter.class.getCanonicalName());
        set.add(Component.class.getCanonicalName());
        set.add(Components.class.getCanonicalName());
        return set;
    }
}
