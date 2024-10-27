package practiceProblems;

class WeatherRecord {
  Date d;
  TemperatureRange today;
  TemperatureRange normal;
  TemperatureRange record;
  int precipitation;

  final int RAINY_PRECIPITATION = 30;

  public WeatherRecord(Date d, TemperatureRange today, TemperatureRange normal,
      TemperatureRange record, int precipitation) {
    this.d = d;
    this.today = today;
    this.normal = normal;
    this.record = record;
    this.precipitation = precipitation;
  }

  boolean withingRange() {
    return this.normal.withinRange(this.today);
  }

  boolean rainyDay() {
    return this.precipitation > this.RAINY_PRECIPITATION;
  }

  boolean recordDay() {
    return !this.record.withinRange(this.today);
  }
}

class Date {
  int day;
  int month;
  int year;

  public Date(int day, int month, int year) {
    this.day = day;
    this.month = month;
    this.year = year;
  }
}

class TemperatureRange{
  int high;
  int low;

  public TemperatureRange(int high, int low) {
    this.high = high;
    this.low = low;
  }

  boolean withinRange(TemperatureRange that) {
    return this.low <= that.low && this.high >= that.high;
  }
}

public class exercise11d2 {
}
