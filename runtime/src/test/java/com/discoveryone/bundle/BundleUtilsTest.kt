package com.discoveryone.bundle

import android.os.Parcel
import android.os.Parcelable
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.Serializable

class BundleUtilsTest {

    @Test
    fun `GIVEN a boolean obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(true.canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a byte obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(0.toByte().canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a char obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue('a'.canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a double obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(3.0.canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a float obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(3f.canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a int obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(3.canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a short obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(3.toShort().canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a long obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(3L.canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a string obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue("".canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a parcelable obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(FakeParcelable().canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a byte array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(byteArrayOf(0).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a boolean array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(booleanArrayOf(true, false).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a char array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(charArrayOf('a', 'b', 'c').canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a double array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(doubleArrayOf(3.0).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a float array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(floatArrayOf(3f).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a int array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(intArrayOf(3).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a long array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(longArrayOf(3L).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a short array WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(shortArrayOf(3).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a serializable obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(FakeSerializable().canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a null obj WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(null.canBeAddedInBundle())
    }

    @Test
    fun `GIVEN a standard obj WHEN checking whether it can be added to a bundle THEN result should be false`() {
        assertFalse(StandardClass().canBeAddedInBundle())
    }

    @Test
    fun `GIVEN an list of parcelable WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(listOf(FakeParcelable(), FakeParcelable()).canBeAddedInBundle())
    }

    @Test
    fun `GIVEN an list of string WHEN checking whether it can be added to a bundle THEN result should be true`() {
        assertTrue(listOf("", "").canBeAddedInBundle())
    }

    private class FakeSerializable : Serializable

    private class StandardClass

    private class FakeParcelable : Parcelable {
        override fun writeToParcel(dest: Parcel?, flags: Int) = Unit

        override fun describeContents(): Int = 0
    }
}