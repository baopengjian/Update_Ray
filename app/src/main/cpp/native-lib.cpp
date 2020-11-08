#include <jni.h>
#include <string>
extern "C" {
    extern int p_main(int argc,char * argv[]);
}


extern "C" JNIEXPORT jstring
JNICALL
Java_bpj_com_update_1ray_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT void JNICALL
Java_bpj_com_update_1ray_MainActivity_bspatch(JNIEnv *env, jobject instance, jstring oldapk_,
                                              jstring patch_, jstring output_) {
    const char *oldapk = env->GetStringUTFChars(oldapk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    char * argv[4] = {"", const_cast<char *>(oldapk), const_cast<char *>(output),
                      const_cast<char *>(patch)};
    p_main(4,argv);
    env->ReleaseStringUTFChars(oldapk_, oldapk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}