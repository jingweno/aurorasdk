/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.auroraide.server.jci.compilers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import org.apache.commons.jci.compilers.AbstractJavaCompiler;
import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompilerSettings;
import org.apache.commons.jci.problems.CompilationProblem;
import org.apache.commons.jci.readers.ResourceReader;
import org.apache.commons.jci.stores.ResourceStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class Jsr199JavaCompiler extends AbstractJavaCompiler {

	private final Log log = LogFactory.getLog(Jsr199JavaCompiler.class);
	private Iterable<String> options;

	private class CompilationUnit extends SimpleJavaFileObject {
		final private ResourceReader reader;
		final private String name;

		public CompilationUnit(final String pName, final ResourceReader pReader) {
			super(URI.create("reader:///" + pName), Kind.SOURCE);
			reader = pReader;
			name = pName;
		}

		public boolean delete() {
			log.debug("delete");
			return super.delete();
		}

		public CharSequence getCharContent(boolean arg0) throws IOException {
			log.debug("getCharContent of " + name);
			byte[] content = reader.getBytes(name);
			return new String(content);
		}

		public Kind getKind() {
			log.debug("getKind" + super.getKind());
			return super.getKind();
		}

		public long getLastModified() {
			log.debug("getLastModified");
			return super.getLastModified();
		}

		public String getName() {
			log.debug("getName " + super.getName());
			return super.getName();
		}

		public boolean isNameCompatible(String arg0, Kind arg1) {
			log.debug("isNameCompatible " + arg0);
			return super.isNameCompatible(arg0, arg1);
		}

		public InputStream openInputStream() throws IOException {
			log.debug("openInputStream");
			return super.openInputStream();
		}

		public OutputStream openOutputStream() throws IOException {
			log.debug("openOutputStream");
			return super.openOutputStream();
		}

		public Reader openReader(boolean arg0) throws IOException {
			log.debug("openReader");
			return super.openReader(arg0);
		}

		public Writer openWriter() throws IOException {
			log.debug("openWriter");
			return super.openWriter();
		}

		public URI toUri() {
			log.debug("toUri " + super.toUri());
			return super.toUri();
		}

	}

	private class JciJavaFileManager implements JavaFileManager {
		@SuppressWarnings("unused")
		private final ResourceStore store;
		final Collection<JavaFileObject> units;

		// final ClassLoader classLoader;

		public JciJavaFileManager(final Collection<JavaFileObject> pUnits,
				final ResourceStore pStore) {

			store = pStore;
			units = pUnits;
			// this.classLoader=classLoader;
		}

		public void close() {
			log.debug("close");
		}

		public void flush() {
			log.debug("flush");
		}

		public ClassLoader getClassLoader(JavaFileManager.Location location) {
			log.debug("getClassLoader");

			return null;

		}

		public FileObject getFileForInput(JavaFileManager.Location location,
				String packageName, String relativeName) {
			log.debug("getFileForInput");
			return null;
		}

		public FileObject getFileForOutput(JavaFileManager.Location location,
				String packageName, String relativeName, FileObject sibling) {
			log.debug("getFileForOutput");
			return null;
		}

		public JavaFileObject getJavaFileForInput(
				JavaFileManager.Location location, String className,
				JavaFileObject.Kind kind) {
			log.debug("getJavaFileForInput");

			return null;
		}

		public JavaFileObject getJavaFileForOutput(
				JavaFileManager.Location location, String className,
				JavaFileObject.Kind kind, FileObject sibling) {
			log.debug("getJavaFileForOutput");
			return null;
		}

		public int isSupportedOption(String option) {
			log.debug("isSupportedOption");
			return 0;
		}

		public boolean handleOption(String current, Iterator<String> remaining) {
			log.debug("handleOption");
			return false;
		}

		public boolean hasLocation(JavaFileManager.Location location) {
			log.debug("hasLocation");
			return false;
		}

		public String inferBinaryName(JavaFileManager.Location location,
				JavaFileObject file) {
			log.debug("inferBinaryName " + file.getName());
			// if(location==StandardLocation.SOURCE_OUTPUT)
			return file.getName().replaceFirst(".java", ".class");
		}

		public Iterable<JavaFileObject> list(JavaFileManager.Location location,
				String packageName, Set<JavaFileObject.Kind> kinds,
				boolean recurse) throws IOException {
			log.debug("list " + location + packageName + kinds + recurse);

			return units;

		}

		public boolean isSameFile(FileObject fileobject, FileObject fileobject1) {
			return false;
		}
	}

	private final Jsr199JavaCompilerSettings settings;

	public Jsr199JavaCompiler() {
		settings = new Jsr199JavaCompilerSettings();
	}

	public Jsr199JavaCompiler(final Jsr199JavaCompilerSettings pSettings) {
		settings = pSettings;
	}

	public CompilationResult compile(final String[] pResourcePaths,
			final ResourceReader pReader, final ResourceStore pStore,
			final ClassLoader classLoader, JavaCompilerSettings settings) {

		final Collection<JavaFileObject> units = new ArrayList<JavaFileObject>();
		for (int i = 0; i < pResourcePaths.length; i++) {
			final String sourcePath = pResourcePaths[i];
			log.debug("compiling " + sourcePath);
			final String source = pResourcePaths[i];
			if (pReader.isAvailable(source))
				units.add(new CompilationUnit(sourcePath, pReader));
		}

		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		final JavaFileManager fileManager = new JciJavaFileManager(units,
				pStore);
		final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		// final JavaFileManager fileManager1 =
		// compiler.getStandardFileManager(null,null,null);

		System.getProperties();

		compiler.getTask(null, fileManager, diagnostics, options, null, units)
				.call();

		try {
			fileManager.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final List<Diagnostic<? extends JavaFileObject>> jsrProblems = diagnostics
				.getDiagnostics();
		final CompilationProblem[] problems = new CompilationProblem[jsrProblems
				.size()];
		int i = 0;
		for (final Diagnostic<? extends JavaFileObject> jsrProblem : jsrProblems) {
			problems[i++] = new Jsr199CompilationProblem(jsrProblem);

			if (problemHandler != null) {
				problemHandler.handle(problems[i - 1]);
			}
		}

		return new CompilationResult(problems);
	}

	public JavaCompilerSettings createDefaultSettings() {
		return this.settings;
	}

	public Iterable<String> getOptions() {
		return options;
	}

	public void setOptions(String... options) {
		this.options = Arrays.asList(options);
	}

}
