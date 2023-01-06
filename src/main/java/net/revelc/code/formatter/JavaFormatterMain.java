/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <<<<<<< HEAD
 * https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * =======
 * >>>>>>> 0d8746abd35e9bbcf7ee541bad74043aa1e5c10d
 * https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.revelc.code.formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.xml.sax.SAXException;

import net.revelc.code.formatter.java.JavaFormatter;
import net.revelc.code.formatter.model.ConfigReadException;
import net.revelc.code.formatter.model.ConfigReader;

/**
 * <p>
 * Class to run Java format for given original code. It's used in bash script which is further used
 * by nvim null-ls plugin formatter - google-java-format.
 * </p>
 * <p>
 * <b>Usage</b>:</br>
 * <code>
 * java -cp $formatter-maven-plugin.jar:$maven-plugin-api.jar
 * net.revelc.code.formatter.JavaFormatterMain
 * </code>
 * </p>
 */
public class JavaFormatterMain {

    private static final LineEnding LINE_ENDING = LineEnding.AUTO;

    public static void main(final String[] args) {
        try {
            // we actually don't need original file as we just want to format given original code
            File originalFile = null;
            String originalCode = null;
            if (args != null && args.length > 0) {
                originalCode = args[0];
            } else {
                originalCode = readSystemInputCode(System.in);
            }
            final Map<String, String> options = readOptions("/jcat-code-formatter.xml");
            final ConfigurationSource configSource = new ConfigurationSourceImpl();

            final JavaFormatter javaFormatter = new JavaFormatter();
            javaFormatter.init(options, configSource);
            final String formattedCode = javaFormatter.formatFile(originalFile, originalCode, LINE_ENDING);

            System.out.print(formattedCode);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String readSystemInputCode(final InputStream in) throws IOException {
        final var fileData = new StringBuilder(1000);
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            var buf = new char[1024];
            var numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                final var readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
        }
        return fileData.toString();
    }

    static Map<String, String> readOptions(final String newConfigFile)
            throws IOException, SAXException, ConfigReadException {
        try (var configInput = JavaFormatterMain.class.getResourceAsStream(newConfigFile)) {
            return new ConfigReader().read(configInput);
        }
    }
}

class ConfigurationSourceImpl extends AbstractMojo implements ConfigurationSource {

    private String compilerSource;
    private String compilerCompliance;
    private String compilerCodegenTargetPlatform;
    private File targetDirectory;
    private Charset encoding;

    ConfigurationSourceImpl(final String compilerSource, final String compilerCompliance,
            final String compilerCodegenTargetPlatform, final File targetDirectory, final Charset encoding) {
        this.compilerSource = compilerSource;
        this.compilerCompliance = compilerCompliance;
        this.compilerCodegenTargetPlatform = compilerCodegenTargetPlatform;
        this.targetDirectory = targetDirectory;
        this.encoding = encoding;
    }

    ConfigurationSourceImpl() {
        this.compilerSource = "1.8";
        this.compilerCompliance = "1.8";
        this.compilerCodegenTargetPlatform = "1.8";
        this.targetDirectory = null;
        this.encoding = StandardCharsets.UTF_8;
    }

    public String getCompilerSources() {
        return this.compilerSource;
    }

    public String getCompilerCompliance() {
        return this.compilerCompliance;
    }

    public String getCompilerCodegenTargetPlatform() {
        return this.compilerCodegenTargetPlatform;
    }

    public File getTargetDirectory() {
        return this.targetDirectory;
    }

    public Charset getEncoding() {
        return this.encoding;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    }
}
