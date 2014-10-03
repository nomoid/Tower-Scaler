package com.github.assisstion.towerScaler.data;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleEncryptionInputStream extends FilterInputStream{

	protected Iterable<Byte> encryptor;
	protected Iterator<Byte> currentIteration;

	public SimpleEncryptionInputStream(InputStream in, Iterable<Byte> encryptor){
		super(in);
		this.encryptor = encryptor;
	}

	public SimpleEncryptionInputStream(InputStream in, byte encryptor){
		this(in, encryptorFromByte(encryptor));
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
	public int read() throws IOException{
		if(currentIteration == null || currentIteration.hasNext() == false){
			currentIteration = encryptor.iterator();
		}
		int b = in.read();
		if(b >= 0){
			b ^= currentIteration.next();
		}
		return b;
	}

	@Override
	public int read(byte[] b) throws IOException{
		byte[] b2 = new byte[b.length];
		int n = in.read(b2);
		int i;
		for(i = 0; i < n;){
			if(currentIteration == null || currentIteration.hasNext() == false){
				currentIteration = encryptor.iterator();
			}
			b[i] = (byte) (b2[i] ^ currentIteration.next());
			i++;
		}
		return i;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException{
		byte[] b2 = new byte[len];
		int n = in.read(b2, off, len);
		int i;
		for(i = 0; i < n;){
			if(currentIteration == null || currentIteration.hasNext() == false){
				currentIteration = encryptor.iterator();
			}
			b[i] = (byte) (b2[i] ^ currentIteration.next());
			i++;
		}
		return i;
	}

	@Override
	public boolean markSupported(){
		return false;
	}

	@Override
	public void mark(int readLimit){
		// Do nothing
	}

	@Override
	public void reset() throws IOException{
		throw new IOException();
	}
}
