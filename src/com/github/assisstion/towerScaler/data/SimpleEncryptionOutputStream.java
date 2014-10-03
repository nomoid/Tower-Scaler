package com.github.assisstion.towerScaler.data;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleEncryptionOutputStream extends FilterOutputStream{

	protected Iterable<Byte> encryptor;
	protected Iterator<Byte> currentIteration;

	public SimpleEncryptionOutputStream(OutputStream out,
			Iterable<Byte> encryptor){
		super(out);
		this.encryptor = encryptor;
	}

	public SimpleEncryptionOutputStream(OutputStream out, byte encryptor){
		this(out, encryptorFromByte(encryptor));
	}

	protected static Iterable<Byte> encryptorFromByte(byte encryptor){
		final byte outerEncryptor = encryptor;
		return new Iterable<Byte>(){

			@Override
			public Iterator<Byte> iterator(){
				return new Iterator<Byte>(){

					boolean used = false;
					byte innerEncryptor = outerEncryptor;

					@Override
					public boolean hasNext(){
						return !used;
					}

					@Override
					public Byte next(){
						if(!used){
							used = true;
							return innerEncryptor;
						}
						else{
							throw new NoSuchElementException();
						}
					}

					@Override
					public void remove(){
						throw new UnsupportedOperationException();
					}

				};
			}

		};
	}

	@Override
	public void write(int b) throws IOException{
		if(currentIteration == null || currentIteration.hasNext() == false){
			currentIteration = encryptor.iterator();
		}
		out.write(b ^ currentIteration.next());
	}

	@Override
	public void write(byte[] b) throws IOException{
		byte[] b2 = new byte[b.length];
		for(int i = 0; i < b.length; i++){
			if(currentIteration == null || currentIteration.hasNext() == false){
				currentIteration = encryptor.iterator();
			}
			b2[i] = (byte) (b[i] ^ currentIteration.next());
		}
		out.write(b2);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException{
		byte[] b2 = new byte[b.length];
		for(int i = 0; i < b.length; i++){
			if(currentIteration == null || currentIteration.hasNext() == false){
				currentIteration = encryptor.iterator();
			}
			b2[i] = (byte) (b[i] ^ currentIteration.next());
		}
		out.write(b2, off, len);
	}
}
