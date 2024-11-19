import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { teachHubGuard } from './teach-hub.guard';

describe('teachHubGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => teachHubGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
