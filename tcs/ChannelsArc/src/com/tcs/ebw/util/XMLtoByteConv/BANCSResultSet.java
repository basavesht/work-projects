package com.tcs.ebw.util.XMLtoByteConv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * @author 231259
 * @year 2008
 */

public class BANCSResultSet extends BANCSResult
{
	static final String version = "BANCSResultSet V1.1";
	private List results = Collections.synchronizedList(new ArrayList());
	private int resultcnt = 0;
	private ListIterator iter;

	public BANCSResultSet()
	{
		super();
		iter = results.listIterator();
		this.setAttribute("_ResultCount", new Integer(0));
	}

	public void addResult(BANCSResult p0)
	{
		try
		{
			synchronized (results)
			{
				iter.add(p0);
			}
			resultcnt++;
			this.setAttribute("_ResultCount", new Integer(resultcnt));
		}
		catch (NullPointerException np)
		{
			np.printStackTrace();
		}
	}

	public void removeResult(BANCSResult p0)
	{
		synchronized (results)
		{
			while (iter.hasNext())
			{
				if (iter.next().equals(p0))
				{
					iter.remove();
					resultcnt--;
					break;
				}
			}
		}
	}

	public BANCSResult getBANCSResult(int p0)
	{
		if (p0 < results.size())
		{
			try
			{
				return (BANCSResult) results.get(p0);
			}
			catch (Exception e)
			{
			}
		}
		return null;
	}

	public BANCSResult getBANCSResult()
	{
		try
		{
			return (BANCSResult) results.get(0);
		}
		catch (Exception e)
		{
		}
		return null;
	}

	public BANCSResult getLastBANCSResult()
	{
		try
		{
			//CHANGED: 12-10-2006 for removing the TermProducts NullPointerException bug
			//Changed results.size() to results.size()-1
			return (BANCSResult) results.get(results.size()-1);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
		return null;
	}

	public void clear()
	{
		this.resultcnt = 0;
		iter = results.listIterator();
		synchronized (results)
		{
			while (iter.hasNext())
			{
				iter.remove();
			}
		}
		this.setAttribute("_ResultCount", new Integer(0));
	}

	public int getResultCount()
	{
		return resultcnt;
	}

	public List getResults()
	{
		return results;
	}
}
