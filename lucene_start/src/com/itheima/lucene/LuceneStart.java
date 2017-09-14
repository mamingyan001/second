package com.itheima.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.packed.DirectReader;
import org.junit.Test;

public class LuceneStart {
	@Test
	public void luceneStart() throws IOException{
	//1.规定lucene构建的目录保存的位置
			//如果是保存在内存中则
			//Directory directory = new RAMDirectory();
		Directory directory = FSDirectory.open(new File("E:/temp"));
	//2.创建indexwriter对象(用于构建索引,需要注入目录以及构建索引类的配置类,配置类中注入分析器)
		Analyzer analyzer = new StandardAnalyzer(); //分析器
		IndexWriterConfig arg1 = new IndexWriterConfig(Version.LATEST, analyzer );
		IndexWriter indexWriter = new IndexWriter(directory, arg1);
	//3.利用流读取源文件
		File file = new File("F:/F盘/java课程/(第13阶段)lucene&solr/01.参考资料/searchsource");
		File[] listFiles = file.listFiles();
		for (File f : listFiles) {
			//名字
			String name = f.getName();
			//内容
			String value = FileUtils.readFileToString(f);
			//路径
			String path = f.getPath();
			//大小
			long size = FileUtils.sizeOf(f);
			//创建document对象
			Document document = new Document();
			//三个参数:1.名字,2.内容,3.是否存储
			Field field1 = new TextField("name",name,Store.YES);
			Field field2 = new TextField("context",value,Store.YES);
			Field field3 = new TextField("path",path,Store.YES);
			Field field4 = new TextField("size",size+"",Store.YES);
			document.add(field1);
			document.add(field2);
			document.add(field3);
			document.add(field4);
			//将document写到索引库
			indexWriter.addDocument(document);
		}
		//提交和关闭
		indexWriter.commit();
		indexWriter.close();
	}
	@Test
	public void searchIndex() throws IOException{
		//1、创建一Directory对象，指定索引库的位置
		Directory directory = FSDirectory.open(new File("E:/temp"));
		//2、创建一个IndexReader对象，以读形式打开索引库
		IndexReader r = DirectoryReader.open(directory);
		//3、创建一个IndexSearcher对象，需要参数：IndexReader
		IndexSearcher indexSearcher = new IndexSearcher(r );
		//4、创建一个Query对象，需要指定要搜索域及要搜索的关键词。
		Query query = new TermQuery(new Term("name","apach"));
		//5、执行搜索，得到一个TopDocs数组。
		TopDocs docs = indexSearcher.search(query, 10);
		//6、遍历TopDocs列表
		ScoreDoc[] scoreDocs = docs.scoreDocs;
		for (ScoreDoc scoreDoc : scoreDocs) {
			//7、取文档id
			int doc = scoreDoc.doc;
			//8、根据文档id取Document对象
			Document document = indexSearcher.doc(doc);
			
		}
		//9、从Document对象中取Filed的内容
		//10、打印结果
		//11、关闭流关闭IndexReader对象
	}
}
