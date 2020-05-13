import { IResource } from 'app/shared/model/resource.model';

export interface IContractType {
  id?: number;
  name?: string;
  resources?: IResource[];
}

export class ContractType implements IContractType {
  constructor(public id?: number, public name?: string, public resources?: IResource[]) {}
}
