import { Moment } from 'moment';

export interface IMonthlyLog {
  id?: string;
  identification?: number;
  reportObtainedBy?: string;
  createdDate?: Moment;
  amount?: number;
  remark?: string;
}

export class MonthlyLog implements IMonthlyLog {
  constructor(
    public id?: string,
    public identification?: number,
    public reportObtainedBy?: string,
    public createdDate?: Moment,
    public amount?: number,
    public remark?: string
  ) {}
}
