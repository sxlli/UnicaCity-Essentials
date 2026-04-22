import org.gradle.api.Plugin
import org.gradle.api.Project
class TestSS implements Plugin<Project> {
    void apply(Project project) {
        project.task("printSS") {
            doLast {
                try {
                    def mClasses = ["net.labymod.api.client.Minecraft", "net.labymod.api.LabyAPI", "net.labymod.api.client.gui.screen.ScreenInstance"]
                    mClasses.each { clz ->
                        try {
                            def cp = Class.forName(clz, false, project.buildscript.classLoader)
                            cp.getMethods().each { m ->
                                if(m.name.toLowerCase().contains("screen") || m.name.toLowerCase().contains("capture")) {
                                    println("METHOD IN " + clz + ": " + m.name)
                                }
                            }
                        }catch(Exception e2){}
                    }
                } catch(Exception e) {}
            }
        }
    }
}
