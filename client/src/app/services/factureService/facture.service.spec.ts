import { TestBed } from '@angular/core/testing';

import { FactureService } from './facture.service';

describe('FactureService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FactureService = TestBed.get(FactureService);
    expect(service).toBeTruthy();
  });
});
