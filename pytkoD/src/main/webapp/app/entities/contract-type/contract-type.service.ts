import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IContractType } from 'app/shared/model/contract-type.model';

type EntityResponseType = HttpResponse<IContractType>;
type EntityArrayResponseType = HttpResponse<IContractType[]>;

@Injectable({ providedIn: 'root' })
export class ContractTypeService {
  public resourceUrl = SERVER_API_URL + 'api/contract-types';

  constructor(protected http: HttpClient) {}

  create(contractType: IContractType): Observable<EntityResponseType> {
    return this.http.post<IContractType>(this.resourceUrl, contractType, { observe: 'response' });
  }

  update(contractType: IContractType): Observable<EntityResponseType> {
    return this.http.put<IContractType>(this.resourceUrl, contractType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContractType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContractType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
