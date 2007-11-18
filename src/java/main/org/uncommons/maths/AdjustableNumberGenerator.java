// ============================================================================
//   Copyright 2006, 2007 Daniel W. Dyer
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
// ============================================================================
package org.uncommons.maths;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Implementation of {@link NumberGenerator} that works similarly to
 * {@link org.uncommons.maths.ConstantGenerator} but allows the returned
 * value to be changed after instantiation.
 * The most useful application of this type of number generator is to permit
 * runtime re-configuration of objects that rely on {@link NumberGenerator}s
 * for their parameters.  This can be achieved by creating UI components (e.g.
 * sliders and spinners) that invoke {@link #setValue(Number)} when their state
 * changes. 
 * @param <T> The type of number generated by this number generator.
 * @author Daniel Dyer
 */
public class AdjustableNumberGenerator<T extends Number> implements NumberGenerator<T>
{
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private T value;

    /**
     * @param value The initial value returned by all invocations of {@link #nextValue()}
     * (until it is modified by a call to {@link #setValue(Number)}.
     */
    public AdjustableNumberGenerator(T value)
    {
        this.value = value;
    }


    /**
     * Change the value that is returned by this generator.
     * @param value The new value to return.
     */
    public void setValue(T value)
    {
        try
        {
            lock.writeLock().lock();
            this.value = value;
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }


    /**
     * {@inheritDoc}
     */
    public T nextValue()
    {
        try
        {
            lock.readLock().lock();
            return value;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
}
