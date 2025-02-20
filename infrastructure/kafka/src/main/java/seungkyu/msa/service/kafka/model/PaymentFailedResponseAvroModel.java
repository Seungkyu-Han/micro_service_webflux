/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package seungkyu.msa.service.kafka.model;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class PaymentFailedResponseAvroModel extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 7665450724872989173L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PaymentFailedResponseAvroModel\",\"namespace\":\"seungkyu.msa.service.kafka.model\",\"fields\":[{\"name\":\"id\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"paymentId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"customerId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"orderId\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"}},{\"name\":\"price\",\"type\":\"long\"},{\"name\":\"createdAt\",\"type\":\"long\"},{\"name\":\"failureMessages\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"string\",\"avro.java.string\":\"String\"}}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<PaymentFailedResponseAvroModel> ENCODER =
      new BinaryMessageEncoder<>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<PaymentFailedResponseAvroModel> DECODER =
      new BinaryMessageDecoder<>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<PaymentFailedResponseAvroModel> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<PaymentFailedResponseAvroModel> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<PaymentFailedResponseAvroModel> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this PaymentFailedResponseAvroModel to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a PaymentFailedResponseAvroModel from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a PaymentFailedResponseAvroModel instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static PaymentFailedResponseAvroModel fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  private java.lang.String id;
  private java.lang.String paymentId;
  private java.lang.String customerId;
  private java.lang.String orderId;
  private long price;
  private long createdAt;
  private java.util.List<java.lang.String> failureMessages;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PaymentFailedResponseAvroModel() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param paymentId The new value for paymentId
   * @param customerId The new value for customerId
   * @param orderId The new value for orderId
   * @param price The new value for price
   * @param createdAt The new value for createdAt
   * @param failureMessages The new value for failureMessages
   */
  public PaymentFailedResponseAvroModel(java.lang.String id, java.lang.String paymentId, java.lang.String customerId, java.lang.String orderId, java.lang.Long price, java.lang.Long createdAt, java.util.List<java.lang.String> failureMessages) {
    this.id = id;
    this.paymentId = paymentId;
    this.customerId = customerId;
    this.orderId = orderId;
    this.price = price;
    this.createdAt = createdAt;
    this.failureMessages = failureMessages;
  }

  @Override
  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }

  // Used by DatumWriter.  Applications should not call.
  @Override
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return paymentId;
    case 2: return customerId;
    case 3: return orderId;
    case 4: return price;
    case 5: return createdAt;
    case 6: return failureMessages;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @Override
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = value$ != null ? value$.toString() : null; break;
    case 1: paymentId = value$ != null ? value$.toString() : null; break;
    case 2: customerId = value$ != null ? value$.toString() : null; break;
    case 3: orderId = value$ != null ? value$.toString() : null; break;
    case 4: price = (java.lang.Long)value$; break;
    case 5: createdAt = (java.lang.Long)value$; break;
    case 6: failureMessages = (java.util.List<java.lang.String>)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.String getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.String value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'paymentId' field.
   * @return The value of the 'paymentId' field.
   */
  public java.lang.String getPaymentId() {
    return paymentId;
  }


  /**
   * Sets the value of the 'paymentId' field.
   * @param value the value to set.
   */
  public void setPaymentId(java.lang.String value) {
    this.paymentId = value;
  }

  /**
   * Gets the value of the 'customerId' field.
   * @return The value of the 'customerId' field.
   */
  public java.lang.String getCustomerId() {
    return customerId;
  }


  /**
   * Sets the value of the 'customerId' field.
   * @param value the value to set.
   */
  public void setCustomerId(java.lang.String value) {
    this.customerId = value;
  }

  /**
   * Gets the value of the 'orderId' field.
   * @return The value of the 'orderId' field.
   */
  public java.lang.String getOrderId() {
    return orderId;
  }


  /**
   * Sets the value of the 'orderId' field.
   * @param value the value to set.
   */
  public void setOrderId(java.lang.String value) {
    this.orderId = value;
  }

  /**
   * Gets the value of the 'price' field.
   * @return The value of the 'price' field.
   */
  public long getPrice() {
    return price;
  }


  /**
   * Sets the value of the 'price' field.
   * @param value the value to set.
   */
  public void setPrice(long value) {
    this.price = value;
  }

  /**
   * Gets the value of the 'createdAt' field.
   * @return The value of the 'createdAt' field.
   */
  public long getCreatedAt() {
    return createdAt;
  }


  /**
   * Sets the value of the 'createdAt' field.
   * @param value the value to set.
   */
  public void setCreatedAt(long value) {
    this.createdAt = value;
  }

  /**
   * Gets the value of the 'failureMessages' field.
   * @return The value of the 'failureMessages' field.
   */
  public java.util.List<java.lang.String> getFailureMessages() {
    return failureMessages;
  }


  /**
   * Sets the value of the 'failureMessages' field.
   * @param value the value to set.
   */
  public void setFailureMessages(java.util.List<java.lang.String> value) {
    this.failureMessages = value;
  }

  /**
   * Creates a new PaymentFailedResponseAvroModel RecordBuilder.
   * @return A new PaymentFailedResponseAvroModel RecordBuilder
   */
  public static seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder newBuilder() {
    return new seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder();
  }

  /**
   * Creates a new PaymentFailedResponseAvroModel RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PaymentFailedResponseAvroModel RecordBuilder
   */
  public static seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder newBuilder(seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder other) {
    if (other == null) {
      return new seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder();
    } else {
      return new seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder(other);
    }
  }

  /**
   * Creates a new PaymentFailedResponseAvroModel RecordBuilder by copying an existing PaymentFailedResponseAvroModel instance.
   * @param other The existing instance to copy.
   * @return A new PaymentFailedResponseAvroModel RecordBuilder
   */
  public static seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder newBuilder(seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel other) {
    if (other == null) {
      return new seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder();
    } else {
      return new seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder(other);
    }
  }

  /**
   * RecordBuilder for PaymentFailedResponseAvroModel instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PaymentFailedResponseAvroModel>
    implements org.apache.avro.data.RecordBuilder<PaymentFailedResponseAvroModel> {

    private java.lang.String id;
    private java.lang.String paymentId;
    private java.lang.String customerId;
    private java.lang.String orderId;
    private long price;
    private long createdAt;
    private java.util.List<java.lang.String> failureMessages;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.paymentId)) {
        this.paymentId = data().deepCopy(fields()[1].schema(), other.paymentId);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.customerId)) {
        this.customerId = data().deepCopy(fields()[2].schema(), other.customerId);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.orderId)) {
        this.orderId = data().deepCopy(fields()[3].schema(), other.orderId);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.price)) {
        this.price = data().deepCopy(fields()[4].schema(), other.price);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
      if (isValidValue(fields()[5], other.createdAt)) {
        this.createdAt = data().deepCopy(fields()[5].schema(), other.createdAt);
        fieldSetFlags()[5] = other.fieldSetFlags()[5];
      }
      if (isValidValue(fields()[6], other.failureMessages)) {
        this.failureMessages = data().deepCopy(fields()[6].schema(), other.failureMessages);
        fieldSetFlags()[6] = other.fieldSetFlags()[6];
      }
    }

    /**
     * Creates a Builder by copying an existing PaymentFailedResponseAvroModel instance
     * @param other The existing instance to copy.
     */
    private Builder(seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.paymentId)) {
        this.paymentId = data().deepCopy(fields()[1].schema(), other.paymentId);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.customerId)) {
        this.customerId = data().deepCopy(fields()[2].schema(), other.customerId);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.orderId)) {
        this.orderId = data().deepCopy(fields()[3].schema(), other.orderId);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.price)) {
        this.price = data().deepCopy(fields()[4].schema(), other.price);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.createdAt)) {
        this.createdAt = data().deepCopy(fields()[5].schema(), other.createdAt);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.failureMessages)) {
        this.failureMessages = data().deepCopy(fields()[6].schema(), other.failureMessages);
        fieldSetFlags()[6] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.String getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder setId(java.lang.String value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'paymentId' field.
      * @return The value.
      */
    public java.lang.String getPaymentId() {
      return paymentId;
    }


    /**
      * Sets the value of the 'paymentId' field.
      * @param value The value of 'paymentId'.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder setPaymentId(java.lang.String value) {
      validate(fields()[1], value);
      this.paymentId = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'paymentId' field has been set.
      * @return True if the 'paymentId' field has been set, false otherwise.
      */
    public boolean hasPaymentId() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'paymentId' field.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder clearPaymentId() {
      paymentId = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'customerId' field.
      * @return The value.
      */
    public java.lang.String getCustomerId() {
      return customerId;
    }


    /**
      * Sets the value of the 'customerId' field.
      * @param value The value of 'customerId'.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder setCustomerId(java.lang.String value) {
      validate(fields()[2], value);
      this.customerId = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'customerId' field has been set.
      * @return True if the 'customerId' field has been set, false otherwise.
      */
    public boolean hasCustomerId() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'customerId' field.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder clearCustomerId() {
      customerId = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'orderId' field.
      * @return The value.
      */
    public java.lang.String getOrderId() {
      return orderId;
    }


    /**
      * Sets the value of the 'orderId' field.
      * @param value The value of 'orderId'.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder setOrderId(java.lang.String value) {
      validate(fields()[3], value);
      this.orderId = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'orderId' field has been set.
      * @return True if the 'orderId' field has been set, false otherwise.
      */
    public boolean hasOrderId() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'orderId' field.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder clearOrderId() {
      orderId = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'price' field.
      * @return The value.
      */
    public long getPrice() {
      return price;
    }


    /**
      * Sets the value of the 'price' field.
      * @param value The value of 'price'.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder setPrice(long value) {
      validate(fields()[4], value);
      this.price = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'price' field has been set.
      * @return True if the 'price' field has been set, false otherwise.
      */
    public boolean hasPrice() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'price' field.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder clearPrice() {
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'createdAt' field.
      * @return The value.
      */
    public long getCreatedAt() {
      return createdAt;
    }


    /**
      * Sets the value of the 'createdAt' field.
      * @param value The value of 'createdAt'.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder setCreatedAt(long value) {
      validate(fields()[5], value);
      this.createdAt = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'createdAt' field has been set.
      * @return True if the 'createdAt' field has been set, false otherwise.
      */
    public boolean hasCreatedAt() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'createdAt' field.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder clearCreatedAt() {
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'failureMessages' field.
      * @return The value.
      */
    public java.util.List<java.lang.String> getFailureMessages() {
      return failureMessages;
    }


    /**
      * Sets the value of the 'failureMessages' field.
      * @param value The value of 'failureMessages'.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder setFailureMessages(java.util.List<java.lang.String> value) {
      validate(fields()[6], value);
      this.failureMessages = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'failureMessages' field has been set.
      * @return True if the 'failureMessages' field has been set, false otherwise.
      */
    public boolean hasFailureMessages() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'failureMessages' field.
      * @return This builder.
      */
    public seungkyu.msa.service.kafka.model.PaymentFailedResponseAvroModel.Builder clearFailureMessages() {
      failureMessages = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PaymentFailedResponseAvroModel build() {
      try {
        PaymentFailedResponseAvroModel record = new PaymentFailedResponseAvroModel();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.String) defaultValue(fields()[0]);
        record.paymentId = fieldSetFlags()[1] ? this.paymentId : (java.lang.String) defaultValue(fields()[1]);
        record.customerId = fieldSetFlags()[2] ? this.customerId : (java.lang.String) defaultValue(fields()[2]);
        record.orderId = fieldSetFlags()[3] ? this.orderId : (java.lang.String) defaultValue(fields()[3]);
        record.price = fieldSetFlags()[4] ? this.price : (java.lang.Long) defaultValue(fields()[4]);
        record.createdAt = fieldSetFlags()[5] ? this.createdAt : (java.lang.Long) defaultValue(fields()[5]);
        record.failureMessages = fieldSetFlags()[6] ? this.failureMessages : (java.util.List<java.lang.String>) defaultValue(fields()[6]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<PaymentFailedResponseAvroModel>
    WRITER$ = (org.apache.avro.io.DatumWriter<PaymentFailedResponseAvroModel>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<PaymentFailedResponseAvroModel>
    READER$ = (org.apache.avro.io.DatumReader<PaymentFailedResponseAvroModel>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.id);

    out.writeString(this.paymentId);

    out.writeString(this.customerId);

    out.writeString(this.orderId);

    out.writeLong(this.price);

    out.writeLong(this.createdAt);

    long size0 = this.failureMessages.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (java.lang.String e0: this.failureMessages) {
      actualSize0++;
      out.startItem();
      out.writeString(e0);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readString();

      this.paymentId = in.readString();

      this.customerId = in.readString();

      this.orderId = in.readString();

      this.price = in.readLong();

      this.createdAt = in.readLong();

      long size0 = in.readArrayStart();
      java.util.List<java.lang.String> a0 = this.failureMessages;
      if (a0 == null) {
        a0 = new SpecificData.Array<java.lang.String>((int)size0, SCHEMA$.getField("failureMessages").schema());
        this.failureMessages = a0;
      } else a0.clear();
      SpecificData.Array<java.lang.String> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<java.lang.String>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          java.lang.String e0 = (ga0 != null ? ga0.peek() : null);
          e0 = in.readString();
          a0.add(e0);
        }
      }

    } else {
      for (int i = 0; i < 7; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readString();
          break;

        case 1:
          this.paymentId = in.readString();
          break;

        case 2:
          this.customerId = in.readString();
          break;

        case 3:
          this.orderId = in.readString();
          break;

        case 4:
          this.price = in.readLong();
          break;

        case 5:
          this.createdAt = in.readLong();
          break;

        case 6:
          long size0 = in.readArrayStart();
          java.util.List<java.lang.String> a0 = this.failureMessages;
          if (a0 == null) {
            a0 = new SpecificData.Array<java.lang.String>((int)size0, SCHEMA$.getField("failureMessages").schema());
            this.failureMessages = a0;
          } else a0.clear();
          SpecificData.Array<java.lang.String> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<java.lang.String>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              java.lang.String e0 = (ga0 != null ? ga0.peek() : null);
              e0 = in.readString();
              a0.add(e0);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










