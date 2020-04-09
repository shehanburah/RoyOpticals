import { Moment } from 'moment';

export interface IDescription {
  id?: string;
  customerId?: string;
  invoiceId?: string;
  sphLeft?: string;
  cylLeft?: string;
  axisLeft?: string;
  readingLeft?: string;
  sphRight?: string;
  cylRight?: string;
  axisRight?: string;
  readingRight?: string;
  orderDate?: Moment;
  deleveryDate?: Moment;
  orderNo?: string;
  frame?: string;
  modelNo?: string;
  size?: string;
  colour?: string;
  lType?: string;
  lSize?: string;
  pd?: string;
  inset?: string;
  name?: string;
  remarks?: string;
}

export class Description implements IDescription {
  constructor(
    public id?: string,
    public customerId?: string,
    public invoiceId?: string,
    public sphLeft?: string,
    public cylLeft?: string,
    public axisLeft?: string,
    public readingLeft?: string,
    public sphRight?: string,
    public cylRight?: string,
    public axisRight?: string,
    public readingRight?: string,
    public orderDate?: Moment,
    public deleveryDate?: Moment,
    public orderNo?: string,
    public frame?: string,
    public modelNo?: string,
    public size?: string,
    public colour?: string,
    public lType?: string,
    public lSize?: string,
    public pd?: string,
    public inset?: string,
    public name?: string,
    public remarks?: string
  ) {}
}
