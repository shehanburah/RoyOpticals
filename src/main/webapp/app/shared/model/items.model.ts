export interface IItems {
  id?: string;
  invoiceId?: string;
  description?: string;
  quantity?: number;
  rate?: number;
  amount?: number;
}

export class Items implements IItems {
  constructor(
    public id?: string,
    public invoiceId?: string,
    public description?: string,
    public quantity?: number,
    public rate?: number,
    public amount?: number
  ) {}
}
