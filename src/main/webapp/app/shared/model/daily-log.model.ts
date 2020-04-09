import { Moment } from 'moment';

export interface IDailyLog {
  id?: string;
  createdDate?: Moment;
  rx?: number;
  invoiceId?: string;
  solderingJobInvoiceId?: string;
  customer?: string;
  customerId?: string;
  description?: string;
  amount?: number;
}

export class DailyLog implements IDailyLog {
  constructor(
    public id?: string,
    public createdDate?: Moment,
    public rx?: number,
    public invoiceId?: string,
    public solderingJobInvoiceId?: string,
    public customer?: string,
    public customerId?: string,
    public description?: string,
    public amount?: number
  ) {}
}
